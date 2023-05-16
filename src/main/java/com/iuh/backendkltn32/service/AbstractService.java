package com.iuh.backendkltn32.service;


public interface AbstractService<T> {
	
	T layTheoMa(String ma) throws RuntimeException;
	T luu(T obj) throws RuntimeException ;
	String xoa(String ma) throws RuntimeException ;
	T capNhat(T obj) throws RuntimeException ;

}
