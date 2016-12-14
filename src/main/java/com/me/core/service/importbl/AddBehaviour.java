package com.me.core.service.importbl;


import com.me.core.domain.entities.Blacklist;

public interface AddBehaviour {
    void importBlacklist(Blacklist blacklist, String path) throws Exception;
}