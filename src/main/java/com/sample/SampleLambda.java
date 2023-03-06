
package com.sample;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;

import java.net.URI;


/**
 * Handler for requests to Lambda function.
 */
public class SampleLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Context awsContext;
    /**
     * Construct an instance suitable for use within the Lambda runtime.
     */
    public SampleLambda() {
    }

    // Analyze the request and route it to the appropriate handler
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        awsContext = context;
        final var response = new APIGatewayProxyResponseEvent();

        try {
            final String method = event.getHttpMethod();
            final String path = event.getPath();
            final String localStackHostname = System.getenv().get("LOCALSTACK_HOSTNAME");

            // Route request to appropriate handler
            if (path.startsWith("/hello")) {
                System.out.println("/hello handler invoked");
                final String tableName = "sample";

                // Try getting secrets
                AwsSessionCredentials awsCreds = AwsSessionCredentials.create(
                        "dummy_access_key_id_here",
                        "dummy_secret_key_id_here",
                        "dummy_session_token_here");
                DynamoDbClient ddb = DynamoDbClient.builder()
                        .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                        .endpointOverride(URI.create("http://" + localStackHostname + ":4566"))
                        .region(Region.US_EAST_1)
                        .build();
                ListTablesResponse listTablesResponse = ddb.listTables();
                System.out.println("list tables response: " + listTablesResponse.toString());

                // Set successful response
                response.setBody("Hello World");
                response.setStatusCode(200);
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            response.setBody(ex.getLocalizedMessage());
            response.setStatusCode(500);
        }
        return response;
    }
}
