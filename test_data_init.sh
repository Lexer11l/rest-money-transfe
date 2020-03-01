# This script is intended only for clean instance run.
# In debug mode it won't return expected result

port=80
userApiURL=http://localhost:$port/api/user
accountApiURL=http://localhost:$port/api/account
transactionApiURL=http://localhost:$port/api/transaction

curl -H "Content-Type: application/json" -d '{"firstName":"Ilya","secondName":"Popov"}' $userApiURL
curl -H "Content-Type: application/json" -d '{"firstName":"Magomed","secondName":"Ozdolev"}' $userApiURL
curl -H "Content-Type: application/json" -d '{"firstName":"Marina","secondName":"Irina"}' $userApiURL

curl -H "Content-Type: application/json" -d '{"ownerUid":1}' $accountApiURL
curl -H "Content-Type: application/json" -d '{"ownerUid":2}' $accountApiURL
curl -H "Content-Type: application/json" -d '{"ownerUid":3}' $accountApiURL

curl -H "Content-Type: application/json" -d '{"toAccount":1, "amount":1000.1}' $transactionApiURL/deposit
curl -H "Content-Type: application/json" -d '{"toAccount":2,"fromAccount":1, "amount":1000}' $transactionApiURL/transfer

curl $accountApiURL/2 # should have balance 1000

