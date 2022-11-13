// SPDX-License-Identifier: MIT

pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";
import 'hardhat/console.sol';

contract LifeCoin is ERC20 {
    constructor() ERC20("LifeCoin", "LIFE") {
        // console.log('Initial supply is %s', initialSupply);
        _mint(msg.sender, 2000000000000);
    }    
}
