pragma solidity >=0.4.22 <0.7.0;
//pragma experimental ABIEncoderV2;
//SPDX-License-Identifier: UNLICENSED

/**
 * @title Image Handling
 * @notice Contract for storing image keypoints, color histogram, metadata and image imageOwner
 * @author Roman Szabo
 */
contract Images {

    constructor() public {
        authorized[msg.sender] = true;
    }

    //Image fingerprint mapping
    mapping(string => bytes32) imageFingerprints;
    //Role ADMIN, API_USER
    mapping(address => bool) authorized;


    /**
     * @notice Add new Image to blockchain storage
     * @param imageId unique Id of image, equal to image id in DB
     * @param imageFingerprint sha256 32 byte fingerprint of image data stored in db
     */
    function newImage(string memory imageId, bytes32 imageFingerprint) public {
        require(authorized[msg.sender], "Only authorized users can interact.");
        imageFingerprints[imageId] = imageFingerprint;
    }

    /**
     * @notice Add authorized users
     * @param _address Adress of the new user
     */
    function addAuthorizedUser(address _address) public {
        require(authorized[msg.sender], "Only authorized users can interact.");
        authorized[_address] = true;
    }

    function remove(string memory id) public {
        require(authorized[msg.sender], "Only authorized users can interact.");
        delete imageFingerprints[id];
    }

    /**
     * @notice Returns all images
     */
    function getImage(string calldata id) external view returns (bytes32) {
        return imageFingerprints[id];
    }

}

