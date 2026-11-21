package br.unicamp.ic.mc322.strategy;

/**
 * Interface base para o padrão Strategy.
 * Implementações específicas de estratégias devem herdar desta interface
 * ou de interfaces derivadas que definam contratos mais específicos.
 */
public interface BuscaStrategy {

    void buscar();
}
