const fs = require('fs');

async function main() {
    const [deployer] = await ethers.getSigners();
    console.log(`Deploying contracts with the account: ${deployer.address}`);

    const balance = await deployer.getBalance();
    console.log(`Account balance: ${balance.toString()}`);

    // Change Sample to your Contract name
    const Contract = await ethers.getContractFactory('LifeCoin');
    const contract = await Contract.deploy();

    console.log(`Contract address: ${contract.address}`);

    const data = {
        address: contract.address,
        abi: JSON.parse(contract.interface.format('json'))
    };

    fs.writeFileSync('frontend/src/Contract.json', JSON.stringify(data));
}

main()
    .then(()=> process.exit(0))
    .catch(error => {
        console.error(error);
        process.exit(1);
    })