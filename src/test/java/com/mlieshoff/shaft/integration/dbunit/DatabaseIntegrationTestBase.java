package com.mlieshoff.shaft.integration.dbunit;

import static org.dbunit.database.DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES;

import static java.util.Objects.requireNonNull;

import com.mlieshoff.shaft.integration.ContainerizedIntegrationTestBase;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.dbunit.Assertion;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
//@Import(TestDbUnitApplicationConfiguration.class)
public abstract class DatabaseIntegrationTestBase extends ContainerizedIntegrationTestBase {

  private static final String COMPOSITE_FILENAME_PATTERN = "dbunit/composites/%s.xml";
  private static final String EXPECTED_SUFFIX = "-expected.xml";
  private static final String FILENAME_PATTERN = "dbunit/%s/%s%s";
  private static final String SETUP_SUFFIX = "-setup.xml";

  protected final Set<String> tablesNamesToDeleteFrom = new HashSet<>();

  @BeforeEach
  public void beforeTestMethod(TestInfo testInfo) throws Exception {
    Method method = testInfo.getTestMethod().orElseThrow();
    String methodName = method.getName();
    DatabaseSetUp databaseSetUp = method.getAnnotation(DatabaseSetUp.class);
    requireNonNull(databaseSetUp, "You need to setup a database setup via @DatabaseSetUp!");
    List<IDataSet> dataSets = new ArrayList<>();
    addCompositesIfNecessary(dataSets, databaseSetUp.dependsOn());
    InputStream inputStream =
        getInputStreamForDataSet(databaseSetUp.value(), methodName, SETUP_SUFFIX);
    dataSets.add(createReplacementDataSet(new FlatXmlDataSetBuilder().build(inputStream)));
    collectTablesForDeletion(dataSets);
    IDatabaseConnection databaseConnection = createDatabaseConnection();
    turnOffConstraintChecking(databaseConnection);
    IDatabaseTester databaseTester = new DefaultDatabaseTester(databaseConnection);
    CompositeDataSet compositeDataSet = new CompositeDataSet(dataSets.toArray(new IDataSet[] {}));
    databaseTester.setDataSet(compositeDataSet);
    databaseTester.onSetup();
  }

  private void addCompositesIfNecessary(List<IDataSet> dataSets, String[] compositeNames)
      throws DataSetException {
    if (ArrayUtils.isNotEmpty(compositeNames)) {
      for (String dependsOn : compositeNames) {
        InputStream compositeInputStream = getInputStreamForCompositeDataSet(dependsOn);
        dataSets.add(
            createReplacementDataSet(new FlatXmlDataSetBuilder().build(compositeInputStream)));
      }
    }
  }

  private InputStream getInputStreamForCompositeDataSet(String dependsOn) {
    String filename = String.format(COMPOSITE_FILENAME_PATTERN, dependsOn);
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
    requireNonNull(
        inputStream,
        "input stream for composite '"
            + dependsOn
            + "' is null! Please check the file is existing in"
            + " `resources/dbunit/composites`! Filename was: "
            + filename);
    return inputStream;
  }

  private ReplacementDataSet createReplacementDataSet(FlatXmlDataSet dataSet) {
    return Replacements.addReplacements(new ReplacementDataSet(dataSet));
  }

  private void collectTablesForDeletion(List<IDataSet> dataSets) throws DataSetException {
    for (IDataSet dataSet : dataSets) {
      String[] tableNames = dataSet.getTableNames();
      if (ArrayUtils.isNotEmpty(tableNames)) {
        Collections.addAll(tablesNamesToDeleteFrom, tableNames);
      }
    }
  }

  private void turnOffConstraintChecking(IDatabaseConnection databaseConnection)
      throws SQLException {
    try (Statement statement = databaseConnection.getConnection().createStatement()) {
      statement.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");
    }
  }

  private InputStream getInputStreamForDataSet(
      String annotationValue, String methodName, String suffix) {
    String specifiedFilename = StringUtils.defaultIfBlank(annotationValue, methodName);
    String filename =
        String.format(FILENAME_PATTERN, getClass().getSimpleName(), specifiedFilename, suffix);
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
    requireNonNull(
        inputStream,
        "input stream for '"
            + suffix
            + "' is null! Please check the file is existing in"
            + " `resources/dbunit/YOUR_TEST`! Filename was: "
            + filename);
    return inputStream;
  }

  private IDatabaseConnection createDatabaseConnection() throws Exception {
    IDatabaseTester databaseTester =
        new JdbcDatabaseTester(
            container.getDriverClassName(),
            container.getJdbcUrl(),
            container.getUsername(),
            container.getPassword());
    IDatabaseConnection connection = databaseTester.getConnection();
    connection.getConfig().setProperty(FEATURE_QUALIFIED_TABLE_NAMES, true);
    return connection;
  }

  @AfterEach
  public void afterTestMethod(TestInfo testInfo) throws Exception {
    Method method = testInfo.getTestMethod().orElseThrow();
    String methodName = method.getName();
    DatabaseExpectation databaseExpectation = method.getAnnotation(DatabaseExpectation.class);
    IDatabaseConnection databaseConnection = createDatabaseConnection();
    IDatabaseTester databaseTester = new DefaultDatabaseTester(databaseConnection);
    if (databaseExpectation != null) {
      List<IDataSet> expectedDataSets = new ArrayList<>();
      addCompositesIfNecessary(expectedDataSets, databaseExpectation.dependsOn());
      InputStream inputStream =
          getInputStreamForDataSet(databaseExpectation.value(), methodName, EXPECTED_SUFFIX);
      expectedDataSets.add(
          createReplacementDataSet(new FlatXmlDataSetBuilder().build(inputStream)));
      for (IDataSet expectedDataSet : expectedDataSets) {
        String[] tableNames = expectedDataSet.getTableNames();
        IDataSet actualDataSet = databaseTester.getConnection().createDataSet();
        for (String tableName : tableNames) {
          ITable expectedTable = expectedDataSet.getTable(tableName);
          ITable actualTable = actualDataSet.getTable(tableName);
          String[] ignoreColumns = getIgnoreColumns(expectedTable, actualTable);
          Assertion.assertEqualsIgnoreCols(expectedTable, actualTable, ignoreColumns);
        }
      }
    }
    databaseTester.onTearDown();
    cleanUpUsedTables(databaseConnection);
  }

  private String[] getIgnoreColumns(ITable expectedTable, ITable actualTable)
      throws DataSetException {
    Set<String> ignoredColumns = new HashSet<>();
    for (Column column : actualTable.getTableMetaData().getColumns()) {
      ignoredColumns.add(column.getColumnName());
    }
    for (Column column : expectedTable.getTableMetaData().getColumns()) {
      ignoredColumns.remove(column.getColumnName());
    }
    return ignoredColumns.toArray(new String[] {});
  }

  private void cleanUpUsedTables(IDatabaseConnection databaseConnection) throws SQLException {
    for (String tableName : tablesNamesToDeleteFrom) {
      try (Statement statement = databaseConnection.getConnection().createStatement()) {
        statement.executeUpdate("delete from " + tableName);
      }
    }
    tablesNamesToDeleteFrom.clear();
  }
}
