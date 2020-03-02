# This script is intended only for clean instance run.
# In debug mode it won't return expected result

port=80
userApiURL=http://localhost:$port/api/user
accountApiURL=http://localhost:$port/api/account
transactionApiURL=http://localhost:$port/api/transaction

curl -H "Content-Type: application/json" -d '{"firstName":"Ilya","secondName":"Popov"}' $userApiURL ; echo
curl -H "Content-Type: application/json" -d '{"firstName":"Magomed","secondName":"Ozdolev"}' $userApiURL ; echo
curl -H "Content-Type: application/json" -d '{"firstName":"Marina","secondName":"Irina"}' $userApiURL ; echo
curl -H "Content-Type: application/json" -d '{"firstName":"Olga","secondName":"Kokorina"}' $userApiURL ; echo
curl -H "Content-Type: application/json" -d '{"firstName":"New","secondName":"User"}' $userApiURL ; echo

curl -H "Content-Type: application/json" -d '{"ownerUid":1}' $accountApiURL ; echo
curl -H "Content-Type: application/json" -d '{"ownerUid":2}' $accountApiURL ; echo
curl -H "Content-Type: application/json" -d '{"ownerUid":3}' $accountApiURL ; echo
curl -H "Content-Type: application/json" -d '{"ownerUid":4}' $accountApiURL ; echo
curl -H "Content-Type: application/json" -d '{"ownerUid":5}' $accountApiURL ; echo

curl -H "Content-Type: application/json" -d '{"toAccount":1, "amount":1000.1}' $transactionApiURL/deposit ; echo
curl -H "Content-Type: application/json" -d '{"toAccount":2,"fromAccount":1, "amount":1000}' $transactionApiURL/transfer ; echo
curl --fail -s $accountApiURL/1 | grep '"balance":0.1' || echo "error, account with id 1 should be 0.1"
curl --fail -s $accountApiURL/2 | grep '"balance":1000' || echo "error, account with id 2 should be 1000"

curl -H "Content-Type: application/json" -d '{"toAccount":3, "amount":10000}' $transactionApiURL/deposit ; echo
curl --fail -s $accountApiURL/3 | grep '"balance":10000' || echo "error, account with id 3 should be 10000"

curl -H "Content-Type: application/json" -d '{"toAccount":4, "amount":-1 }' --fail -s  $transactionApiURL/deposit  #| grep "Transaction amount should be positive number" || echo "Request should produce error"
curl --fail -s $accountApiURL/4 | grep '"balance":0' || echo "error, account with id 4 should be 0"

curl -H "Content-Type: application/json" -d '{"toAccount":5, "amount":1000}' $transactionApiURL/deposit ; echo
curl --fail -s $accountApiURL/5 | grep '"balance":1000' || echo "error, account with id 5 should be 1000"

curl -s -H "Content-Type: application/json" -d '{"fromAccount":5, "amount":1000.1 }' --fail -s  $transactionApiURL/withdraw  #| grep "Not enough money to make operation" || echo "Request should produce error"
curl --fail -s $accountApiURL/5 | grep '"balance":1000' || echo "error, account with id 5 should be 1000"

curl -H "Content-Type: application/json" -d '{"fromAccount":5, "amount":1000}' $transactionApiURL/withdraw ; echo
curl --fail -s $accountApiURL/5 | grep '"balance":0' || echo "error, account with id 5 should be 0"




