# Image Checker Smart Contracts
## Contents
* _build_ - compiled output of contracts from truffle
* _contracts_ - Solidity contracts
* _migrations_ - files used by the truffle to deploy
* __truffle-config.js__ - truffle configuration (network, wallet)
## About
Truffle project for deploying Smart Contracts to Azure Ethereum Network. [Details](https://docs.microsoft.com/en-us/azure/blockchain/templates/ethereum-poa-deployment#ethereum-development). <br>
[Truffle](https://www.trufflesuite.com/) suite is easy to use to deploy contracts to Blockchain using simple cmd commands. <br>
`truffle migrate --network poa` - to deploy contracts

### Library
* Truffle Suite for smart contract deployment: [Truffle](https://www.trufflesuite.com/) <br>
https://www.trufflesuite.com/

## Contracts
Smart Contract used is written is solidity language. _contracts_/__Images.sol__ is contract used in application.
Using authorized mapping to allow modification only to set users.
## Azure Ethereum Setup
Used default settings during from *Ethereum Proof-of-Authority Consortium* Azure Store item. <br>
https://docs.microsoft.com/de-de/azure/blockchain/templates/ethereum-poa-deployment
## Blockchain content
https://github.com/gobitfly/etherchain-light <br>
Etherchain project is running on VM to provide blockchain explorer functionality on custom chain.
No modification done, just runs to provide the functionality, not in project scope.