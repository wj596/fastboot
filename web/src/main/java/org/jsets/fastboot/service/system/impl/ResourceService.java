package org.jsets.fastboot.service.system.impl;

import org.jsets.fastboot.frame.service.impl.BaseService;
import org.jsets.fastboot.mapper.system.ResourceMapper;
import org.jsets.fastboot.model.entity.system.Resource;
import org.jsets.fastboot.service.system.IResourceService;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends BaseService<Resource, ResourceMapper> implements IResourceService {

}