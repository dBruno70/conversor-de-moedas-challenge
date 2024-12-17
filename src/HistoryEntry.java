public record HistoryEntry(Exchange.Currency base, Exchange.Currency target, double value, double result) {
    @Override
    public String toString() {
        return "%s %s -> %s %s".formatted(
                Exchange.prettyNumber(value),
                Exchange.getCurrencyNameString(base),
                Exchange.prettyNumber(result),
                Exchange.getCurrencyNameString(target)
                );
    }
}
