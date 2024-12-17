public record ExchangeRateApiResult(
        String result,
        ConversionRates conversion_rates
) {
    public record ConversionRates(
            double USD, double BRL, double EUR
    ) {}
}
