import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class Exchange {

    public enum Currency {
        BRL,
        USD,
        EUR
    }

    private final HashMap<Currency, Double> rates = new HashMap<>();

    public Exchange(Currency base, String key) {
        URI uri = URI.create("https://v6.exchangerate-api.com/v6/" + key + "/latest/" + getCurrencyNameString(base));

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            ExchangeRateApiResult apiResult = gson.fromJson(response.body(), ExchangeRateApiResult.class);
            extractRates(apiResult);

        } catch (Exception err) {
            System.out.println("Erro ao obter informações de conversão de moeda.");
        }
    }

    private void extractRates(ExchangeRateApiResult result) {
        rates.put(Currency.BRL, result.conversion_rates().BRL());
        rates.put(Currency.USD, result.conversion_rates().USD());
        rates.put(Currency.EUR, result.conversion_rates().EUR());
    }

    public double convert(Currency target, double value) {
        return value * rates.get(target);
    }

    public static String getCurrencyNameString(Currency currency) {
        return switch (currency) {
            case BRL -> "BRL";
            case USD -> "USD";
            case EUR -> "EUR";
        };
    }

    public static String prettyNumber(double number) {
        return "%.2f".formatted(number).replace(".", ",");
    }
}