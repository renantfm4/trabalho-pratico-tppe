package br.unb.tppe.curation;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suite de Testes")
@SelectClasses({
        EquivalenciaNomesTest.class,
        DeduplicacaoIdTest.class,
        ExcecaoTest.class
})
public class SuiteGeralTest {
}