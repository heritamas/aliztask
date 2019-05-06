package folder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import folder.model.TreeItem;

public class FolderHandler 
{
	TreeItem root;
	
	public FolderHandler(String root) {
		super();
		this.root = new TreeItem(root);
	}
	
	public TreeItem getTree() {
		return root;
	}

	public TreeItem addFolder(String folder, BiFunction<TreeItem, Boolean, Void> setter, boolean arg) {
		LinkedList<String> dirs = new LinkedList<>(Arrays.asList(folder.split("/")));
		
		root.buildTree(dirs, setter, arg);
		
		return root;
	}

	public TreeItem buildTreeStructureFromList(List<String> folderList, BiFunction<TreeItem, Boolean, Void> setter, boolean arg) {
		
		Iterator<String> itr = folderList.iterator();
		
		while (itr.hasNext()) {
			addFolder(itr.next(), setter, arg);
		}
		
		return root;
	}
	
	/**
	 * Creates a TreeItem, in which all the writable, on at least readable path residing folders are contained
	 * This solution is not recommended - the algorithm is too complex
	 * @return - transformed tree (nodes possibly removed)
	 * @throws CloneNotSupportedException 
	 */
	public TreeItem getWritablesComplex() throws CloneNotSupportedException {
		
		TreeItem writables = root.clone();
		writables.calculateLeastReadableAttribute(true);
		writables.calculateWritables();
		
		return writables;
	}

	/**
	 * Creates a TreeItem, in which all the writable, on at least readable path residing folders are contained 
	 * @return - transformed tree (nodes possibly removed)
	 * @throws CloneNotSupportedException 
	 */
	public TreeItem getWritablesSimple() throws CloneNotSupportedException {
		
		TreeItem writables = root.clone();
		writables.calculateLeastReadableAttribute(true);
		writables.deleteNotLeastReadables();
		
		return writables;
	}
}
