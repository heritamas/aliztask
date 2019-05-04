package folder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import folder.model.TreeItem;

public class FolderHandler 
{
	
	public static TreeItem addFolder(TreeItem tree, String folder, BiFunction<TreeItem, Boolean, Void> setter, boolean arg) {
		LinkedList<String> dirs = new LinkedList<>(Arrays.asList(folder.split("/")));
		
		tree.buildTree(dirs, setter, arg);
		
		return tree;
	}

	public static TreeItem buildTreeStructureFromList(TreeItem structure, List<String> folderList, BiFunction<TreeItem, Boolean, Void> setter, boolean arg) {
		
		Iterator<String> itr = folderList.iterator();
		
		while (itr.hasNext()) {
			FolderHandler.addFolder(structure, itr.next(), setter, arg);
		}
		
		return structure;
	}
	
	/**
	 * Create a TreeItem, from the TreeItem given as parameters, in which all the writable, 
	 * on at least readable path residing folders are contained 
	 * @param structure - input tree to be transformed
	 * @return - transformed tree (nodes possibly removed)
	 * @throws CloneNotSupportedException 
	 */
	public static TreeItem getWritablesComplex(TreeItem structure) throws CloneNotSupportedException {
		
		TreeItem writables = structure.clone();
		writables.calculateLeastReadableAttribute(true);
		writables.calculateWritables();
		
		return writables;
	}

	/**
	 * Create a TreeItem, from the TreeItem given as parameters, in which all the writable, 
	 * on at least readable path residing folders are contained 
	 * @param structure - input tree to be transformed
	 * @return - transformed tree (nodes possibly removed)
	 * @throws CloneNotSupportedException 
	 */
	public static TreeItem getWritablesSimple(TreeItem structure) throws CloneNotSupportedException {
		
		TreeItem writables = structure.clone();
		writables.calculateLeastReadableAttribute(true);
		writables.deleteNotLeastReadables();
		
		return writables;
	}
}
