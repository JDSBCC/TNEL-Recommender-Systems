package utilities;

public class TreeNode {
	private TreeNode parent;
	private TreeNode[] children = new TreeNode[0];
	private int m_userData;

	public TreeNode() {}

	public TreeNode(int userObject) {
		m_userData = userObject;
	}

	public void add(TreeNode child, int index) {
		// Add the child to the list of children.
		if (index < 0 || index == children.length) // then append
		{
			TreeNode[] newChildren = new TreeNode[children.length + 1];
			System.arraycopy(children, 0, newChildren, 0, children.length);
			newChildren[children.length] = child;
			children = newChildren;
		} else if (index > children.length) {
			throw new IllegalArgumentException("Cannot add child to index "
					+ index + ".  There are only " + children.length
					+ " children.");
		} else // insert
		{
			TreeNode[] newChildren = new TreeNode[children.length + 1];
			if (index > 0) {
				System.arraycopy(children, 0, newChildren, 0, index);
			}
			newChildren[index] = child;
			System.arraycopy(children, index, newChildren, index + 1,
					children.length - index);
			children = newChildren;
		}

		// Set the parent of the child.
		child.parent = this;
	}
	
	public void add(TreeNode child) {
		add(child, -1);
	}
	
	public TreeNode remove(int index) {
		if (index < 0 || index >= children.length)
			throw new IllegalArgumentException(
					"Cannot remove element with index " + index
							+ " when there are " + children.length
							+ " elements.");

		// Get a handle to the node being removed.
		TreeNode node = children[index];
		node.parent = null;

		// Remove the child from this node.
		TreeNode[] newChildren = new TreeNode[children.length - 1];
		if (index > 0) {
			System.arraycopy(children, 0, newChildren, 0, index);
		}
		if (index != children.length - 1) {
			System.arraycopy(children, index + 1, newChildren, index,
					children.length - index - 1);
		}
		children = newChildren;

		return node;
	}

	public void removeFromParent() {
		if (parent != null) {
			int position = this.index();
			parent.remove(position);
			parent = null;
		}
	}

	public TreeNode getParent() {
		return parent;
	}

	public boolean isRoot() {
		if (parent == null) {
			return true;
		} else {
			return false;
		}
	}

	public TreeNode[] children() {
		return children;
	}

	public boolean hasChildren() {
		if (children.length == 0) {
			return false;
		} else {
			return true;
		}
	}

	public int index() {
		if (parent != null) {
			for (int i = 0;; i++) {
				Object node = parent.children[i];

				if (this == node) {
					return i;
				}
			}
		}

		// Only ever make it here if this is the root node.
		return -1;
	}

	public int depth() {
		int depth = recurseDepth(parent, 0);
		return depth;
	}

	private int recurseDepth(TreeNode node, int depth) {
		if (node == null) // reached top of tree
		{
			return depth;
		} else {
			return recurseDepth(node.parent, depth + 1);
		}
	}

	public void setUserObject(int userObject) {
		m_userData = userObject;
	}

	public int getUserObject() {
		return m_userData;
	}
}
