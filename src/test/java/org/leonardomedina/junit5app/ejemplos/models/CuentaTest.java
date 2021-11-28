package org.leonardomedina.junit5app.ejemplos.models;

import org.junit.jupiter.api.Test;
import org.leonardomedina.junit5app.ejemplos.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal(100.12));

        //affirmacion - assert
        String esperado = "Leonardo";
        String actual = cuenta.getPersona();
        assertNotNull(actual);
        assertEquals(esperado, actual);
        assertTrue( esperado.equals(esperado));
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
    void testDebitoCueta() {
        Cuenta cuenta = new Cuenta("Leonardo",  new BigDecimal("1000.12"));
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCueta() {
        Cuenta cuenta = new Cuenta("Leonardo",  new BigDecimal("1000.12"));
        cuenta.credto(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal( "1000.12"));
        assertEquals(1000.12, cuenta.getSaldo().doubleValue() );
        assertFalse( cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0 );// saldo negativo menor a cero devuelve -1
        assertTrue( cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0 );
        assertNotNull(cuenta.getSaldo());
    }

    @Test
    void testDineroInsuficienteException() {
        Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
        Exception catchExceptio = assertThrows(DineroInsuficienteException.class, ()-> {
            cuenta.debito(new BigDecimal(1500));
        });

        String actual = catchExceptio.getMessage();
        String esperado = "Dinero Insuficiente";

        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroTest() {
        Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
        Cuenta cuenta2 = new Cuenta("Lorena", new BigDecimal("2000.23"));

        Banco banco = new Banco("Bano Pichincha");

        banco.transferir(cuenta, cuenta2, new BigDecimal(200));

        assertEquals("2200.23", cuenta2.getSaldo().toPlainString());
        assertEquals("800.23", cuenta.getSaldo().toPlainString());
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
                .filter( c -> c.getPersona().equals("Lorena"))
                .findFirst()
                .get().getPersona()
        );
        assertTrue( banco.getCuentas()
                .stream()
                .anyMatch( c -> c.getPersona().equals("Leonardo"))
        );
        assertTrue( banco.getCuentas()
                .stream()
                .filter( c -> c.getPersona().equals("Lorena"))
                .findFirst()
                .isPresent()
        );
    }

    @Test
    void testRelacionBancoCuentaAssertAll() {
        Cuenta cuenta = new Cuenta("Leonardo", new BigDecimal("1000.23"));
        Cuenta cuenta2 = new Cuenta("Lorena", new BigDecimal("2000.23"));

        Banco banco = new Banco("Banco Pichincha");
        banco.addCuenta(cuenta);
        banco.addCuenta(cuenta2);


        banco.transferir(cuenta, cuenta2, new BigDecimal(200));

        // assertAll( ()->{}, ()->{}, ()->{}, ()->{}, ()->{});
        assertAll(
                ()-> assertEquals("2200.23", cuenta2.getSaldo().toPlainString()),
                ()-> assertEquals("800.23", cuenta.getSaldo().toPlainString()),
                ()-> assertEquals(2, banco.getCuentas().size()),
                ()-> assertEquals("Banco Pichincha", cuenta.getBanco().getNombre()),
                ()-> {
                    assertEquals("Lorena", banco.getCuentas()
                            .stream()
                            .filter( c -> c.getPersona().equals("Lorena"))
                            .findFirst()
                            .get().getPersona()
                    );
                },
                ()->{
                    assertTrue( banco.getCuentas()
                            .stream()
                            .anyMatch( c -> c.getPersona().equals("Leonardo"))
                    );
                },
                ()->{
                    assertTrue( banco.getCuentas()
                            .stream()
                            .filter( c -> c.getPersona().equals("Lorena"))
                            .findFirst()
                            .isPresent()
                    );
                }
        );

    }
}