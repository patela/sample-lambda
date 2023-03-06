# sample-lambda
Simple AWS Lambda which has one endpoint

To deploy using serverless, use the following command:

```shell
serverless deploy --stage local --region us-east-1
```

After deploy completes, you should see a message in output like this:

```shell
✔ Service deployed to stack sample-app-local (28s)

endpoint: http://localhost:4566/restapis/u2cw487nt5/local/_user_request_
```

Use `curl` to request the /hello endpoint, like this:

```shell
curl http://localhost:4566/restapis/u2cw487nt5/local/_user_request_/hello
```

The response should be:

```shell
Hello World!
```