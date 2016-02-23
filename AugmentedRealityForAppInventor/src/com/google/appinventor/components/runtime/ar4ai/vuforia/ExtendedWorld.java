package com.google.appinventor.components.runtime.ar4ai.vuforia;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.threed.jpct.World;

public class ExtendedWorld {
	Map<String, World> world_list;
	Map<String, WorldInfo> world_info_list;

	private static final long serialVersionUID = 1L;

	public ExtendedWorld() {
		this.world_list = new HashMap<String, World>();
		this.world_info_list = new HashMap<String, WorldInfo>();

	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public World getWorld(String id) {
		return this.world_list.get(id);
	}

	public WorldInfo getInfo(String id) {
		return this.world_info_list.get(id);
	}

	public Collection<World> getWorldList() {
		return this.world_list.values();
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<String, World>> getWorldEntrySet() {
		return this.world_list.entrySet();
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<Entry<String, WorldInfo>> getInfoEntrySet() {
		return this.world_info_list.entrySet();
	}

	public Collection<WorldInfo> getInfoList() {
		return this.world_info_list.values();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public World putWorld(String key, World value) {
		return world_list.put(key, value);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public WorldInfo putInfo(String key, WorldInfo value) {
		return world_info_list.put(key, value);
	}

}
