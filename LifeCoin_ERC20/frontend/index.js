const Web3 = require('web3');
const contract = require('./src/Contract.json');
const contractAddress = '0xe4067f2726B7Ea7fdc27ABee72bb59Eb7523E608'
const privateKey = '9867ffeec2a95c04bf63071624e6b815137bcef4e6e0fa1740f7643b55667dbf';
const fromAddress = '0x5bfCbB1b529a30136aadBd4D00644A7a6167cB81';
const toAddress = '0xc00686d32BBD083002d82CFf60D6931a5DE6904B';

const init1 = async() => {
    const web3 = new Web3("https://sepolia.infura.io/v3/b9674cd6ccea4fccbf84b5dffb388616");
    const networkId = await web3.eth.net.getId();
    const myContract = new web3.eth.Contract(
        contract.abi,
        contractAddress
    );

    const tx = myContract.methods.setData(10).send({
        from: fromAddress
    });
    const gas = await tx.estimateGas({from: fromAddress});
    const gasPrice = await web3.eth.getGasPrice();
    const data = tx.encodeABI();
    const nonce = await web3.eth.getTransactionCount(fromAddress);

    const signedTx = await web3.eth.accounts.signTransaction(
        {
            to: toAddress,
            data,
            gas,
            gasPrice,
            nonce,
            chainId: networkId
        },
        privateKey
    );

    console.log(`Old data value ${await myContract.methods.data().call()}`);
    const receipt = await web3.eth.sendTransaction(signedTx.rawTransaction);
    console.log(`Transaction hash: ${receipt.transactionHash}`)
    console.log(`New data value ${await myContract.methods.data().call()}`);
}

init1();