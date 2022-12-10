package com.store.services;

import java.util.List;

import com.store.entity.Producto;

public interface IProductoService {

	public List<Producto> findAll();

	public Producto findById(Long id);

	public Producto save(Producto productoActual);
}
