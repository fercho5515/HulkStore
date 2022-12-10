package com.store.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.store.dao.IProductoDao;
import com.store.entity.Producto;

public class ValidarExistenciaService {

	@Autowired
	private IProductoDao productoDao;
	
	public boolean validarCantidad(Producto producto) {
		Producto procutoCantidadTotal = productoDao.findById(producto.getId()).orElseThrow(IllegalArgumentException::new);
		if(producto.getCantidad() > procutoCantidadTotal.getCantidad()) {
			return false;
		}
		return true;
	}
}
