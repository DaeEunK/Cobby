import styled from "styled-components";

export const Contatiner = styled.div`
  position: absolute;
  font-family: "DungGeunMo";
  width : 100%;
  height: 100%;
  background-color: rgba(89, 89, 89, 0.5);  
  z-index: 10;
`;

export const Modal = styled.div`
  position: fixed;
  display: block;
  z-index: 15;
  width: 300px;
  height: 200px;
  border-radius: 5px;
  overflow: hidden;
  background-color: #ffffff;
  transition: color 0s 0.1s;
  text-align: center;
  color: #3a3a3a;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

export const ModalInfo = styled.div`
  padding-top: 15%;
`;

export const ModalButton = styled.button`
  font-family: "DungGeunMo";
  width: 70px;
  height: 35px;
  border: none;
  border-radius: 5px;
  background-color: #ededdb;
  color: #3a3a3a;

  margin-top: 30px;
  //   box-shadow: 0 6px 10px -2px gray;
  cursor: pointer;

  :hover {
    width: 75px;
    height: 40px;
    background-color: #3a3a3a;
    color: white;
    transition: 0.3s;
  }
`;

export const CloseButton = styled.div`
  float: right;
  margin-right: 7px;
  margin-top: 5px;
  color: gray;
  cursor: pointer;
`;
