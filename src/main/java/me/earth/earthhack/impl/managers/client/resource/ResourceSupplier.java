package me.earth.earthhack.impl.managers.client.resource;

import me.earth.earthhack.impl.managers.client.resource.ResourceException;
import net.minecraft.client.resources.IResource;

@FunctionalInterface
public interface ResourceSupplier {
    public IResource get() throws ResourceException;
}
