package com.store.dao;

import org.springframework.data.repository.CrudRepository;

import com.store.entity.Producto;

public interface IProductoDao extends CrudRepository <Producto, Long>{

}
