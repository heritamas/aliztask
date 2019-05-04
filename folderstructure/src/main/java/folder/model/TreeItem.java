package folder.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiFunction;

public class TreeItem implements Cloneable {
	
	private String name;
	
	private boolean readable = false;
	private boolean writable = false;
	private boolean onLeastReadablePath = false;
	
	private List<TreeItem> children = new ArrayList<>();
	
	public TreeItem(String name) {
		super();
		this.name = name;
	}
	
	@Override
	public TreeItem clone() throws CloneNotSupportedException  {
		TreeItem cloned = (TreeItem) super.clone();

		//cloned.name = new String(name);
		cloned.children = new ArrayList<>();
		for (TreeItem ti : getChildren()) {
			cloned.getChildren().add(ti.clone());
		}
		
		return cloned;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isReadable() {
		return readable;
	}

	public Void setReadable(boolean readable) {
		this.readable = readable;
		
		return null;
	}

	public boolean isWritable() {
		return writable;
	}

	public Void setWritable(boolean writable) {
		this.writable = writable;
		
		return null;
	}
	
	public List<TreeItem> getChildren() {
		return children;
	}

	/**
	 * Get (if exists) or create a child with name
	 * @param name - name of child
	 * @return TreeItem found or created
	 */
	private TreeItem getChild(String name) {
		
		Optional<TreeItem> found = children
			.stream()
			.filter( str -> str.getName().equals(name))
			.findFirst();
		
		if (found.isPresent()) {
			return found.get();
		} else {
			TreeItem newItem = new TreeItem(name);
			children.add(newItem);
			return newItem;
		}
	}
	
	private void checkAttributes() {
		if (isWritable()) {
			// being writable has the consequence of being readable
			setReadable(true);
		}
	}
	
	public TreeItem buildTree(Queue<String> leafs, BiFunction<TreeItem, Boolean, Void> setter, boolean arg) {
		
		if (leafs.isEmpty()) {
			//use setter
			setter.apply(this, arg);
			checkAttributes();
			
			return this;
		} 
		
		// leaf to be inserted
		String nextDir = leafs.poll();
		if (name.equals(nextDir)) {
			// we already have it, descend with the rest of leafs
			return buildTree(leafs, setter, arg);
		} else {
			// get matching tree item
			TreeItem child = getChild(nextDir);
			
			// find in children, and descend in tree with the rest of leafs
			return child.buildTree(leafs, setter, arg);
		}
	}

	/**
	 * recursive walk to determine and set least readable attribute
	 * @param leastReadable - should parent be considered least readable 
	 */
	public void calculateLeastReadableAttribute(boolean leastReadable) {
		onLeastReadablePath = leastReadable && isReadable();

		if (onLeastReadablePath) {
			// recursive descent
			
			for (TreeItem child : getChildren()) {
				child.calculateLeastReadableAttribute(onLeastReadablePath);
			}
		} else {
			// nothing under this will be reachable, return from descent
			return;
		}
	}
	
	/**
	 * Deletes subtrees, that are not accessible on a least readable path
	 * @return
	 */
	public boolean deleteNotLeastReadables() {
		
		if (onLeastReadablePath) {
			
			ArrayList<TreeItem> toBeRemoved = new ArrayList<>();
			for (TreeItem ti : getChildren()) {
				if (ti.deleteNotLeastReadables()) {
					toBeRemoved.add(ti);
				}
			}
			getChildren().removeAll(toBeRemoved);
			
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Deletes children or items that do not qualify for the conditions of the task 
	 * Recursive algorithm, paying attention to the "onLeastReadablePath" and "writable" attributes
	 * @return should this node be removed
	 */
	public boolean calculateWritables() {
		
		// accessible?
		if (onLeastReadablePath) {
			
			if (getChildren().isEmpty()) {
				// this is a leaf
				if (isWritable()) {
					// if writable, accessible folder, leave it
					return false;
				} else {
					// otherwise - delete it
					return true;
				}
			} else {
				// inner node; we are on least readable path, so we can descend recursively
				
				// not every child will be good - mark children for removals
				ArrayList<TreeItem> toBeRemoved = new ArrayList<>();
				for (TreeItem ti : getChildren()) {
					if (ti.calculateWritables()) {
						toBeRemoved.add(ti);
					}
				}
				getChildren().removeAll(toBeRemoved);

				// re-evaluate (could become leaf due to removals)
				if (getChildren().isEmpty()) {
					// if leaf -
					return !isWritable();
				} else {
					return false;
				}
				
			}
			
		} else {
			return true;
		}
	}
	
	/**
	 * Gets all writable folder names accessible from root on a least readable path.
	 * Prerequisite: calculateLeastReadableAttribute(), calculateWritables() OR deleteNotLeastReadables()
	 * @param prefix full path name of parent
	 * @param showAttrs show attributes in output
	 * @return
	 */
	public List<String> getWritablePaths(String prefix, boolean showAttrs) {
		
		List<String> paths = new ArrayList<>();
		String actualPath = prefix == null ? name : prefix + "/" + name;
		String attrs = String.format(" (%s%s)", 
				readable ? "rd" : "",
				writable ? "wr" : ""
			);
		
		if (isWritable()) {
			paths.add(actualPath  + (showAttrs ? attrs : ""));
		}
		
		for (TreeItem ti : getChildren()) {
			paths.addAll(ti.getPaths(actualPath, showAttrs));
		}
		
		return paths;
	}


	@Override
	public String toString() {
		return String.format("(name=%s, rd=%b, wr=%b, olrd=%b, children=%s)", name, readable, writable, onLeastReadablePath, children);
	}

}
