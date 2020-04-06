
```
gradle wrapper
./gradlew bootRun
```
You'll need a `.env` file that contains: 

    TOPIC_ID, OPERATOR_ID, OPERATOR_KEY, NETWORK_NAME, MIRROR_NODE_ADDRESS, SUBMIT_KEY (optional), API_USERNAME, and API_PASSWORD (they will be set to these values)

Spring Boot Demo that creates a HederaConsensusService that asynchronously posts entities to HCS on save. 
