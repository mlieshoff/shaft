package com.mlieshoff.shaft.integration.dbunit;

import org.dbunit.dataset.ReplacementDataSet;

class Replacements {

    static ReplacementDataSet addReplacements(ReplacementDataSet replacementDataSet) {
        replacementDataSet.addReplacementObject("###NULL###", null);
        return replacementDataSet;
    }
}
