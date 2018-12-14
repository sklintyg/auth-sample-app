package se.inera.intyg.authsampleapp.service.token;

public interface TokenExchangeService {
    String exchange(String samlAssertion);

    String exchangePreStored();
}
