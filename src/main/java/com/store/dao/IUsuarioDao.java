package com.store.dao;

import org.springframework.data.repository.CrudRepository;

import com.store.entity.Usuario;

public interface IUsuarioDao extends CrudRepository <Usuario, Long>{

	public Usuario findByNombre(String username);
}
