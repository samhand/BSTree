package com.myjava.datastruct.bst;

public class BSTreeNode <T extends Comparable<T>>{
		private T value;
		private BSTreeNode<T>  left;
		private BSTreeNode<T> right;
		private BSTreeNode<T> parent;
		
		BSTreeNode(){
			
		}
		BSTreeNode(T value){
			this.value=value;
		}
		
		public T getValue(){
			return value;
		}
		public void setValue(T value){
			this.value=value;
		}
		public BSTreeNode<T> getLeft(){
			return left;
		}
		public void setLeft(BSTreeNode<T> left){
			this.left=left;
		}
		public BSTreeNode<T> getRight(){
			return right;
		}
		public void setRight(BSTreeNode<T> right){
			this.right=right;
		}
		public BSTreeNode<T> getParent(){
			return parent;
		}
		public void setParent(BSTreeNode<T> parent){
			this.parent=parent;
		}
		
		/**
		 *  is leaf node
		 */
		public boolean isLeaf(){
			return left==null && right==null;
		}
		
		@Override
		public String toString(){
			return value.toString();
		}
	
}














