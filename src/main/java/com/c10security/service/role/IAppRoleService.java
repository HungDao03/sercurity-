package com.c10security.service.role;

import com.c10security.model.AppRole;
import com.c10security.service.GeneralService;

public interface IAppRoleService extends GeneralService<AppRole> {
    AppRole findByName(String name);
}