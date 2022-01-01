package org.leonardomedina.junit5app.ejemplos.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.leonardomedina.junit5app.ejemplos.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {

    Cuenta cuenta;

    @BeforeAll
    static void beforeAll() {
        System.out.println("=============================== INICIALIZA TEST ========================================");
    }

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Leonardo", new BigDecimal(100.12));
        System.out.println("iniciando el metodo.... ");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizar el metodo");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("=============================== FINALIZANDO TEST ========================================");

    }

    @Nested
    @DisplayName("probando atributos de cuenta corriente")
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("PROBANDO_NOMBRE_CUENTA")
        void testNombreCuenta() {
            //cuenta = new Cuenta("Leonardo", new BigDecimal(100.12));

            //affirmacion - assert
            String esperado = "Leonardo";
            String actual = cuenta.getPersona();
            assertNotNull(actual, "la cuenta no puede ser nula");
            assertEquals(esperado, actual, "El nombre de la cuenta nos es el que se esperaba");
            assertTrue(esperado.equals(esperado), "Nombre cuenta esperada debe ser igual a la real");
        }

        @Test
        void testReferenciaDeCuenta() {
            Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("2000.12"));
            Cuenta cuenta2 = new Cuenta("Leonardo.", new BigDecimal("2000.12"));


            /**
             *   //compara por referencia
             *
             *   Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("2000.12"));
             *   Cuenta cuenta2 = new Cuenta("Leonardo", new BigDecimal("2000.12"));
             *
             *   assertNotEquals(cuenta, cuenta2); // true, aun que los objetos sean iguales, el assert dice que no sean iguales
             */
            // assertNotEquals(cuenta, cuenta2);
            assertNotEquals(cuenta, cuenta2);
        }


        @Test
        @DisplayName("Probando el sando de la cuenta corriente, que no sea null, mayor que cero, valor esperado")
        void testSaldoCuenta() {
            cuenta = new Cuenta("Leonardo", new BigDecimal("1000.12"));
            assertEquals(1000.12, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);// saldo negativo menor a cero devuelve -1
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            assertNotNull(cuenta.getSaldo());
        }

        @Test
        @DisplayName("testeando referencias que sean iguales con el método equals")
        void testDineroInsuficienteException() {
            Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
            Exception catchExceptio = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal(1500));
            });

            String actual = catchExceptio.getMessage();
            String esperado = "Dinero Insuficiente";

            assertEquals(esperado, actual);
        }

        @Test
        void testRelacionBancoCuenta() {
            Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
            Cuenta cuenta2 = new Cuenta("Lorena", new BigDecimal("2000.23"));

            Banco banco = new Banco("Banco Pichincha");
            banco.addCuenta(cuenta);
            banco.addCuenta(cuenta2);


            banco.transferir(cuenta, cuenta2, new BigDecimal(200));

            assertEquals("2200.23", cuenta2.getSaldo().toPlainString());
            assertEquals("800.23", cuenta.getSaldo().toPlainString());

            assertEquals(2, banco.getCuentas().size());
            assertEquals("Banco Pichincha", cuenta.getBanco().getNombre());
            assertEquals("Lorena", banco.getCuentas()
                    .stream()
                    .filter(c -> c.getPersona().equals("Lorena"))
                    .findFirst()
                    .get().getPersona()
            );
            assertTrue(banco.getCuentas()
                    .stream()
                    .anyMatch(c -> c.getPersona().equals("Leonardo"))
            );
            assertTrue(banco.getCuentas()
                    .stream()
                    .filter(c -> c.getPersona().equals("Lorena"))
                    .findFirst()
                    .isPresent()
            );
        }

        @Test
        @Disabled
        @DisplayName("probando relaciones entre cuentas y el banco con ASSERT ALL")
        void testRelacionBancoCuentaAssertAll() {
            //fail(); //FUERZA A QUE LA PRUEBA UNITARIA FALLE
            Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
            Cuenta cuenta2 = new Cuenta("Lorena", new BigDecimal("2000.23"));

            Banco banco = new Banco("Banco Pichincha");
            banco.addCuenta(cuenta);
            banco.addCuenta(cuenta2);


            banco.transferir(cuenta, cuenta2, new BigDecimal(200));

            // assertAll( ()->{}, ()->{}, ()->{}, ()->{}, ()->{});
            assertAll(
                    () -> assertEquals("2200.23", cuenta2.getSaldo().toPlainString()),
                    () -> assertEquals("800.23", cuenta.getSaldo().toPlainString()),
                    () -> assertEquals(2, banco.getCuentas().size()),
                    () -> assertEquals("Banco Pichincha", cuenta.getBanco().getNombre()),
                    () -> {
                        assertEquals("Lorena", banco.getCuentas()
                                .stream()
                                .filter(c -> c.getPersona().equals("Lorena"))
                                .findFirst()
                                .get().getPersona()
                        );
                    },
                    () -> {
                        assertTrue(banco.getCuentas()
                                .stream()
                                .anyMatch(c -> c.getPersona().equals("Leonardo"))
                        );
                    },
                    () -> {
                        assertTrue(banco.getCuentas()
                                .stream()
                                .filter(c -> c.getPersona().equals("Lorena"))
                                .findFirst()
                                .isPresent()
                        );
                    }
            );

        }

    }

    @Nested
    class CuentaOperacionTest {
        @Test
        void testDebitoCueta() {
            cuenta = new Cuenta("Leonardo", new BigDecimal("1000.12"));
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testCreditoCueta() {
            Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.12"));
            cuenta.credto(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testTransferirDineroTest() {
            cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
            Cuenta cuenta2 = new Cuenta("Lorena", new BigDecimal("2000.23"));

            Banco banco = new Banco("Bano Pichincha");

            banco.transferir(cuenta, cuenta2, new BigDecimal(200));

            assertEquals("2200.23", cuenta2.getSaldo().toPlainString());
            assertEquals("800.23", cuenta.getSaldo().toPlainString());
        }

    }

    @Nested
    class SistemaOperativoTest {

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinux() {
            System.out.println("++++++++++++++++++ PRUEBA TEST EN LINX +++++++++++++++++++++");
        }

        @Test
        @EnabledOnOs({OS.WINDOWS})
        void testSoloWindowns() {
            System.out.println("++++++++++++++++++ PRUEBA TEST EN WINDOWS +++++++++++++++++++++");
        }

        @Test
        @DisabledOnOs({OS.WINDOWS})
        void testNOWindowns() {
            System.out.println("++++++++++++++++++ PRUEBA TEST NO WINDOWS +++++++++++++++++++++");
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {

        }

        @Test
        @EnabledOnJre(JRE.JAVA_11)
        void soloJdk11() {

        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + "" + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*1.8.*")
        void testJavaVersionProperties() {

        }


        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*1.8.*")
        void testJavaHome() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadore() {

        }
    }

    @Nested
    class VariblesAmbiente {

        @Test
        void imprimirValiablesDeAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + "" + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT_LEO", matches = "dev")
        void testEnv() {

        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT_LEO", matches = "prod")
        void testEnvProdDisabled() {

        }

        /**
         * Valiables virtuales de la maquina virtual de JAVA
         */
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testEnvDev() {
        }
    }

    /**
     * Ejecucion de test condicionales
     * - assertion es para afirmar un valor de verdad
     * - assumption es para asumir un valor de verdad
     * <p>
     * -ea -DENV=dev
     */
    @Nested
    class AssumptionTest {
        @Test
        @DisplayName("test Saldo Cuenta Dev")
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);
            cuenta = new Cuenta("Leonardo", new BigDecimal("1000.12"));
            assertEquals(1000.12, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);// saldo negativo menor a cero devuelve -1
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            assertNotNull(cuenta.getSaldo());
        }

        @Test
        @DisplayName("test Saldo Cuenta Dev Asumming That")
        void testSaldoCuentaDevAsummingThat() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, () -> {
                cuenta = new Cuenta("Leonardo", new BigDecimal("1000.12"));
                assertEquals(1000.12, cuenta.getSaldo().doubleValue());
                assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);// saldo negativo menor a cero devuelve -1
                assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
                assertNotNull(cuenta.getSaldo());
            });
        }
    }


    @RepeatedTest(value=5, name="Repetición #{currentRepetition} de {totalRepetitions}")
    void testDebitoCueta(RepetitionInfo i) {
        if( i.getCurrentRepetition() == 3){
            System.out.println("===================== entro en la repetición 3");
        }
        cuenta = new Cuenta("Leonardo", new BigDecimal("1000.12"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12", cuenta.getSaldo().toPlainString());
    }
}