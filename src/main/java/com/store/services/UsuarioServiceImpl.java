package com.store.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.store.dao.IUsuarioDao;
import com.store.entity.Usuario;
import com.store.utils.RSAController;

@Service
public class UsuarioServiceImpl implements IUsuarioService{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	public Usuario save(Usuario usuario) {
		RSAController rsa = new RSAController();
		String pkey = rsa.getKey();
		
		try {
			usuario.setNombre(rsa.decrypt(usuario.getNombre(), pkey));
			
		}catch(Exception ex) {
			
			return null;
		}
		if(validarNombreExiste(usuario)) {
			return null;
		}
		return usuarioDao.save(usuario);
	}
	
	public boolean validarNombreExiste(Usuario usuario) {
		
		try {
			Usuario userValida = usuarioDao.findByNombre(usuario.getNombre());
			
			if(userValida == null) {
				return false;
			}
		
			return true;
		}catch(Exception ex) {
			
			return false;
		}
	}
}
