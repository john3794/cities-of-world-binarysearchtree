import java.util.Comparator;

public class BinarySearchTree implements Comparator<City>
{
    private Node root;

    public BinarySearchTree()
    {
        this.root = null;
    }

    public Node getRoot()
    {
        return root;
    }

    @Override
    public int compare(City c1, City c2)
    {
        int value;
        value = c1.getCityName().compareTo(c2.getCityName());
        return value;
    }

    public int compareCountry(City c1, City c2)
    {
        int value;
        value = c1.getCountryName().trim().toLowerCase().compareTo(c2.getCountryName().trim().toLowerCase());
        return value;
    }

    static class Node
    {
        City data;
        Node left;
        Node right;

        Node(City data)
        {
            this.data = data;
            left = null;
            right = null;
        }
    }

    /**
     * Insert city by longitude
     *
     * @param city
     */
    public void insert(City city)
    {
        Node newNode = new Node(city);

        if (root == null)
        {
            root = newNode;
            return;
        }

        Node current = root;
        Node parent;

        while (true)
        {
            parent = current;
            if (city.getLongDecimal() < current.data.getLongDecimal())
            {
                current = current.left;
                if (current == null)
                {
                    System.out.println("Insert " + newNode.data.getCityName() + newNode.data.getCountryName() +
                            " to left of " + parent.data.getCityName() + parent.data.getCountryName());
                    parent.left = newNode;
                    return;
                }
            }
            else
            {
                current = current.right;
                if (current == null)
                {
                    System.out.println("Insert " + newNode.data.getCityName() + newNode.data.getCountryName() +
                            " to right of " + parent.data.getCityName() + parent.data.getCountryName());
                    parent.right = newNode;
                    return;
                }
            }
        }
    }

    /**
     * Print BST in order (ascending)
     *
     * @param node
     */
    public void printTreeInOrder(Node node)
    {
        if (node != null)
        {
            printTreeInOrder(node.left);
            System.out.println("Traversed " + node.data.getCountryName() + " " + node.data.getCityName());
            printTreeInOrder(node.right);
        }
    }

    /**
     * Insert city by alphabetical order
     *
     * @param city
     */
    public void insertAlpha(City city)
    {
        Node newNode = new Node(city);

        if (root == null)
        {
            root = newNode;
            return;
        }

        Node current = root;
        Node parent;

        while (true)
        {
            parent = current;
            if (compare(city, root.data) < 0)
            {
                current = current.left;
                if (current == null)
                {
                    System.out.println("Insert " + newNode.data.getCityName() + newNode.data.getCountryName() +
                            " <-- " + parent.data.getCityName() + parent.data.getCountryName());
                    parent.left = newNode;
                    return;
                }
            }
            else
            {
                current = current.right;
                if (current == null)
                {
                    System.out.println("Insert " + newNode.data.getCityName() + newNode.data.getCountryName() +
                            " --> " + parent.data.getCityName() + parent.data.getCountryName());
                    parent.right = newNode;
                    return;
                }
            }
        }
    }

    /**
     * Find city by traversing through binary search tree alphabetical order
     *
     * @param cityName
     * @return
     */
    public City find(String cityName)
    {
        Node current = root;

        City city = new City(cityName, null);

        while (current != null)
        {
            if (current.data.getCityName().equalsIgnoreCase(cityName))
            {
                return current.data;
            }
            else if (compare(city, current.data) < 0)
            {
                current = current.left;
            }
            else
            {
                current = current.right;
            }
        }
        return null;
    }

    /**
     * Returns city-node by given country
     *
     * @param node
     * @param countryName
     * @return
     */
    public Node findCityFrom(Node node, String countryName)
    {
        City city = new City(null, countryName);

        if (node != null)
        {
            if (compareCountry(node.data, city) == 0)
            {
                return node;
            }
            else
            {
                Node foundNode = findCityFrom(node.left, countryName);
                if (foundNode == null)
                {
                    foundNode = findCityFrom(node.right, countryName);
                }
                return foundNode;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Delete city-node from BST
     *
     * @param city
     * @return
     */
    public boolean delete(City city)
    {
        Node parent = root;
        Node current = root;
        boolean isLeftChild = false;

        while (compare(current.data, city) != 0)
        {
            parent = current;
            if (compare(current.data, city) > 0)
            {
                isLeftChild = true;
                current = current.left;
            }
            else
            {
                isLeftChild = false;
                current = current.right;
            }
            if (current == null)
            {
                return false;
            }
        }

        /** NODE FOUND: */
        //Case 1: if node to be deleted has no children
        if (current.left == null && current.right == null)
        {
            if (current == root) root = null;
            if (isLeftChild) parent.left = null;
            else parent.right = null;
        }
        //Case 2 : if node to be deleted has only one child
        else if (current.right == null)
        {
            if (current == root) root = current.left;
            else if (isLeftChild) parent.left = current.left;
            else parent.right = current.left;
        }
        else if (current.left == null)
        {
            if (current == root) root = current.right;
            else if (isLeftChild) parent.left = current.right;
            else parent.right = current.right;
        }
        //Case 3: node has 2 children
        else if (current.left != null && current.right != null)
        {
            //now we have found the minimum element in the right sub tree
            Node successor = getSuccessor(current);

            if (current == root) root = successor;
            else if (isLeftChild) parent.left = successor;
            else parent.right = successor;

            successor.left = current.left;
        }
        return true;
    }

    /**
     * Find successor to replace the deleted node
     *
     * @param deleteNode
     * @return
     */
    public Node getSuccessor(Node deleteNode)
    {
        Node successor = null;
        Node successorParent = null;
        Node current = deleteNode.right;

        while (current != null)
        {
            successorParent = successor;
            successor = current;
            current = current.left;
        }

        // check if successor has the right child, it cannot have left child for sure
        // if it does have the right child, add it to the left of successorParent.
        if (successor != deleteNode.right)
        {
            successorParent.left = successor.right;
            successor.right = deleteNode.right;
        }
        return successor;
    }

}
