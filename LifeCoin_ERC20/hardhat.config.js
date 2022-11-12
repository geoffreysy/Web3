/** @type import('hardhat/config').HardhatUserConfig */
require('@nomiclabs/hardhat-waffle');

const INFURA_URL = "https://sepolia.infura.io/v3/b9674cd6ccea4fccbf84b5dffb388616";
const PRIVATE_KEY = "9867ffeec2a95c04bf63071624e6b815137bcef4e6e0fa1740f7643b55667dbf";

module.exports = {
  solidity: "0.8.17",
  networks: {
    sepolia: {
      url: INFURA_URL,
      accounts: [`0x${PRIVATE_KEY}`]
    }
  }
};