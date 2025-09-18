package br.ufpb.dcx.dsc.repositorios.exception;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String message){
        super(message);
    }
}
