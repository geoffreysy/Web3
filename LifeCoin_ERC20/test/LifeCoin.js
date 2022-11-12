const { expect } = require('chai');

describe ('Testing contract', () => {
    let Contract, contract, owner, addr1, addr2;

    beforeEach(async() => {

        // change Sample to contract name
        Contract = await ethers.getContractFactory('LifeCoin');

        // change 1000 to contract parameters
        contract = await Contract.deploy();
        [owner, addr1, addr2, _] = await ethers.getSigners();

    })

    describe('Deployment', () => {
        /*
        it ('Should set the right owner', async() => {
            expect(await contract.owner()).to.equal(owner.address);
        })
        */

        it ('Should assign the total supply of tokens to the owner', async() => {
            const ownerBalance = await contract.balanceOf(owner.address);
            expect(await contract.totalSupply()).to.equal(ownerBalance);
        })
    })

    describe('Transactions', () => {
        it ('Should transfer tokens between accounts', async()=> {
            await contract.transfer(addr1.address, 50);
            const addr1Balance = await contract.balanceOf(addr1.address);
            expect(addr1Balance).to.equal(50);

            await contract.connect(addr1).transfer(addr2.address, 50);
            const addr2Balance = await contract.balanceOf(addr2.address);
            expect(addr2Balance).to.equal(50);

        })
    })

    it('Should fail if sender doesnt have enough tokens', async() => {
        const initialBalanceOwner = await contract.balanceOf(owner.address);

        await expect(contract.connect(addr1).transfer(owner.address, 1))
            .to.be.revertedWith('ERC20: transfer amount exceeds balance');        
        expect(await contract.balanceOf(owner.address)).to.equal(initialBalanceOwner)
    })

    it('Should update balance after transfers', async() => {
        const initialBalanceOwner = await contract.balanceOf(owner.address);

        await contract.transfer(addr1.address, 100);
        await contract.transfer(addr2.address, 50);

        // const finalOwnerBalance = await contract.balanceOf(owner.address);
        // expect (finalOwnerBalance).to.equal(initialBalanceOwner-150);

        const addr1Balance = await contract.balanceOf(addr1.address);
        expect(addr1Balance).to.equal(100);

        const addr2Balance = await contract.balanceOf(addr2.address);
        expect(addr2Balance).to.equal(50);


    })
});