package fr.redstonneur1256.redutilities.io.zip;

import fr.redstonneur1256.redutilities.function.Provider;
import fr.redstonneur1256.redutilities.io.zip.strategy.DiscStrategy;
import fr.redstonneur1256.redutilities.io.zip.strategy.ZipStrategy;

public enum Strategy {

    disc(DiscStrategy::new);

    private Provider<ZipStrategy> strategy;

    Strategy(Provider<ZipStrategy> strategy) {
        this.strategy = strategy;
    }

    public Provider<ZipStrategy> getStrategy() {
        return strategy;
    }

}
