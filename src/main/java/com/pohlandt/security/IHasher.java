package com.pohlandt.security;

import org.apache.shiro.util.ByteSource;

public interface IHasher {
	public String hash(String text, ByteSource salt); 
}
