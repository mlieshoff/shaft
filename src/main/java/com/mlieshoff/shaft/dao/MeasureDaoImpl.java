package com.mlieshoff.shaft.dao;

import com.google.common.hash.Hashing;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MeasureDaoImpl implements MeasureDao {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public LocationDaoDto createLocation(String database, String bucket) {
    Session session = getSession();
    long hashDatabase = getHash(database);
    long hashBucket = getHash(bucket);
    LocationEntity locationEntity = new LocationEntity(hashDatabase, hashBucket);
    updateIntern(session, locationEntity, hashDatabase, TrackerTypes.ID, database);
    updateIntern(session, locationEntity, hashBucket, TrackerTypes.ID, bucket);
    return MeasureDaoMapper.INSTANCE.locationEntityToLocationDaoDto(locationEntity);
  }

  @Override
  public <I extends Comparable<? super I>, V extends Comparable<? super V>, M extends Measure<V>>
      M update(LocationDaoDto locationDaoDto, I id, MeasureType measureType, V value) {
    Session session = getSession();
    LocationEntity locationEntity =
        new LocationEntity(locationDaoDto.databaseHash(), locationDaoDto.bucketHash());
    long hash = getHash(id);
    updateIntern(session, locationEntity, hash, TrackerTypes.ID, id);
    return updateIntern(session, locationEntity, hash, measureType, value);
  }

  private Session getSession() {
    return entityManager.unwrap(Session.class);
  }

  private <I extends Comparable<? super I>> long getHash(I id) {
    String stringifiedId;
    if (id instanceof String) {
      stringifiedId = (String) id;
    } else {
      stringifiedId = String.valueOf(id);
    }
    return Hashing.md5().hashString(stringifiedId, StandardCharsets.UTF_8).asLong();
  }

  private <V extends Comparable<? super V>, M extends Measure<V>> M updateIntern(
      Session session, LocationEntity locationEntity, long hash, MeasureType measureType, V value) {
    Class<V> valueClass = (Class<V>) measureType.getValueClass();

    MeasureId measureId = new MeasureId();
    measureId.setDatabaseHash(locationEntity.databaseHash());
    measureId.setBucketHash(locationEntity.bucketHash());
    measureId.setHash(hash);
    measureId.setModifiedAt(new Date());
    measureId.setType(measureType.getCode());

    M measure = createMeasure(valueClass);
    measure.setValue(value);
    measure.setMeasureId(measureId);

    List<M> old =
        (List<M>)
            session
                .createQuery(
                    "from "
                        + measure.getClass().getName()
                        + " t where t.measureId.databaseHash=:database "
                        + "and t.measureId.bucketHash=:bucket "
                        + "and t.measureId.hash=:hash "
                        + "and t.measureId.type=:type "
                        + "order by t.measureId.modifiedAt desc")
                .setParameter("database", measureId.getDatabaseHash())
                .setParameter("bucket", measureId.getBucketHash())
                .setParameter("hash", measureId.getHash())
                .setParameter("type", measureId.getType())
                .setMaxResults(1)
                .list();

    M first = null;
    boolean change = false;

    if (CollectionUtils.isNotEmpty(old)) {
      first = old.get(0);
      change = ObjectUtils.compare(first.getValue(), value) != 0;
    }

    if (CollectionUtils.isEmpty(old) || change) {
      session.save(measure);
    }

    return first;
  }

  private <V extends Comparable<? super V>, M extends Measure<V>> M createMeasure(Class<V> clazz) {
    Class<M> measureClass = findMeasureClass(clazz);
    try {
      return measureClass.getConstructor().newInstance();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private <M extends Measure<V>, V extends Comparable<? super V>> Class<M> findMeasureClass(
      Class<V> clazz) {
    if (String.class == clazz) {
      return (Class<M>) StringMeasure.class;
    } else if (Double.class == clazz) {
      return (Class<M>) DecimalMeasure.class;
    }
    return null;
  }

  @Override
  public <I extends Comparable<? super I>, V extends Comparable<? super V>, M extends Measure<V>>
      Optional<M> findCurrent(LocationEntity locationEntity, MeasureType measureType, I id) {
    Class<V> valueClass = (Class<V>) measureType.getValueClass();
    Class<M> measureClass = findMeasureClass(valueClass);
    Session session = getSession();
    long hash = getHash(id);
    return (Optional<M>)
        session
            .createQuery(
                "from "
                    + measureClass.getName()
                    + " t where t.measureId.hash=:hash and t.measureId.type=:type order by t.measureId.modifiedAt desc")
            .setParameter("hash", hash)
            .setParameter("type", measureType.getCode())
            .setMaxResults(1)
            .uniqueResult();
  }

  @Override
  public <I extends Comparable<? super I>, V extends Comparable<? super V>, M extends Measure<V>>
      List<M> getLastX(LocationEntity locationEntity, MeasureType measureType, I id, int limit) {
    Class<V> valueClass = (Class<V>) measureType.getValueClass();
    Class<M> measureClass = findMeasureClass(valueClass);
    Session session = getSession();
    long hash = getHash(id);
    return session
        .createQuery(
            "from "
                + measureClass.getName()
                + " t where t.measureId.hash=:hash and t.measureId.type=:type order by t.measureId.modifiedAt desc")
        .setParameter("hash", hash)
        .setParameter("type", measureType.getCode())
        .setMaxResults(limit)
        .list();
  }

  @Override
  public <V extends Comparable<? super V>, M extends Measure<V>> Long count(
      LocationEntity locationEntity, MeasureType measureType) {
    Class<V> valueClass = (Class<V>) measureType.getValueClass();
    Class<M> measureClass = findMeasureClass(valueClass);
    Session session = getSession();
    return ((BigInteger)
            session
                .createNativeQuery("select count(*) from " + measureClass.getName())
                .uniqueResult())
        .longValue();
  }
}
