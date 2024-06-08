package ch.twidev.invodb.driver.mongodb;

import ch.twidev.invodb.bridge.driver.InvoClusterDriver;
import ch.twidev.invodb.bridge.driver.InvoDriverType;
import ch.twidev.invodb.bridge.driver.config.DriverConfig;
import ch.twidev.invodb.bridge.driver.config.URLDriverConfig;
import ch.twidev.invodb.bridge.exceptions.DriverConfigException;
import ch.twidev.invodb.bridge.exceptions.DriverConnectionException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.CompletableFuture;

public class MongoCluster extends InvoClusterDriver<MongoDatabase, MongoConnection> {

    private MongoClient mongoClient;

    public MongoCluster(URLDriverConfig driverConfig) throws DriverConnectionException {
        super(driverConfig, InvoDriverType.MONGODB);

        this.initDriver();
    }

    @Override
    public void initDriver() throws DriverConnectionException {
        DriverConfig driverConfig = this.getDriverConfig();

        if(driverConfig instanceof URLDriverConfig urlDriverConfig) {
            this.mongoClient = MongoClients.create(urlDriverConfig.getUrl());
        }else{
            throw new DriverConfigException("Invalid configuration for MongoCluster");
        }
    }

    @Override
    public void close() {
        mongoClient.close();
    }

    @Override
    public boolean exists() {
        return mongoClient != null;
    }

    @Override
    public MongoConnection connectSession(String keyname) {
        return new MongoConnection(mongoClient.getDatabase(keyname));
    }

    @Override
    @Deprecated
    public CompletableFuture<MongoConnection> connectSessionAsync(String keyname) {
        return null;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
