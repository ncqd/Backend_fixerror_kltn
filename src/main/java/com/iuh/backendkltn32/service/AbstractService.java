package com.iuh.backendkltn32.service;


public interface AbstractService<T> {
	
	T layTheoMa(String ma) throws RuntimeException;
	T luu(T obj) throws Exception ;
	String xoa(String ma) throws Exception ;
	T capNhat(T obj) throws Exception ;

}
