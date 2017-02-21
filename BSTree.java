package com.myjava.datastruct.bst;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

import com.myjava.datastruct.bst.BSTreeNode;

public class BSTree <T extends Comparable<T>>{
			final private  BSTreeNode<T> root;
			// �ڵ���
			AtomicLong size=new AtomicLong(0);
			
			// ����ģʽʱ,���нڵ�û����ͬ��ֵ
			// �Ǹ���ģʽʱ,�ڵ��������ֵͬ,���鲻Ҫʹ�÷Ǹ���ģʽ
			private volatile boolean overrideMode=true;
			
			public BSTree(){
				this.root=new BSTreeNode<T>();
			}
			
			public BSTree(boolean overrideMode){
				this();
				this.overrideMode=overrideMode;
			}
			
			public boolean isOverrideMode(){
				return overrideMode;
			}
			
			public void setOverrideMode(boolean overrideMode){
				this.overrideMode=overrideMode;
			}
			
			/**
			 * ���Ľڵ���
			 * @return
			 */
			public long getSize(){
				return size.get();
			}
			
			/**
			 * ��ȡ���ڵ�
			 * @return
			 */
			private BSTreeNode<T> getRoot(){
				return root.getLeft();
			}
			
			/**
			 * ����һ���µĽڵ�
			 * ���ֵ�棬�򷵻ش���ֵ�����򷵻ؿա�
			 * ������ڸ���ģʽ��ֵ�Ѿ���������
			 * ��Ҫ���Ǿ�ֵ
			 * 
			 * @param value
			 * @return
			 */
			public T addNode(T value){
				BSTreeNode<T> t=new BSTreeNode<T>(value);
				return addNode(t);
			}
			
			/**
			 * ������ֵ���ҽڵ�(����key,key��������,
			 *����������ʹ��,@see compare method).��������ڣ����ؿ�
			 * @param key
			 * @return
			 */
			public BSTreeNode<T> find(T key){
				BSTreeNode<T> dataRoot=new BSTreeNode<T>();
				dataRoot=getRoot();
				while(dataRoot!=null){
					int cmp=dataRoot.getValue().compareTo(key);
					if(cmp==0){
						return dataRoot;
					}else if(cmp<0){
						dataRoot=dataRoot.getLeft();
					}else {
						dataRoot=dataRoot.getRight();
					}
				}
				return null;
			}
			
			private T addNode(BSTreeNode<T> node){
				node.setLeft(null);
				node.setRight(null);
				setParent(node,null);
				if(root.getLeft()==null){
					root.setLeft(node);
					size.incrementAndGet();
				}
				else{
			           BSTreeNode<T> x = findParentNode(node); 
			            int cmp = x.getValue().compareTo(node.getValue()); 
			 
			            if(this.overrideMode && cmp==0){ 
			                T v = x.getValue(); 
			                x.setValue(node.getValue()); 
			                return v; 
			            }else if(cmp==0){ 
			                //�Ѿ��и�ֵ�������
			                return x.getValue(); 
			            } 
			 
			            setParent(node,x); 
			 
			            if(cmp>0){ 
			                x.setLeft(node); 
			            }else{ 
			                x.setRight(node); 
			            } 
			 
			            size.incrementAndGet(); 
				}
				return null;
			}
		    /** 
		     * �ҵ��ڵ�x�ĸ��ڵ�
		     * @param x 
		     * @return 
		     */ 
		    private BSTreeNode<T> findParentNode(BSTreeNode<T> x){ 
		        BSTreeNode<T> dataRoot = getRoot(); 
		        BSTreeNode<T> child = dataRoot; 
		 
		        while(child!=null){ 
		            int cmp = child.getValue().compareTo(x.getValue()); 
		            if(cmp==0){ 
		                return child; 
		            } 
		            if(cmp>0){ 
		                dataRoot = child; 
		                child = child.getLeft(); 
		            }else if(cmp<0){ 
		                dataRoot = child; 
		                child = child.getRight(); 
		            } 
		        } 
		        return dataRoot; 
		    } 
		    
			private void setParent(BSTreeNode<T> node,BSTreeNode<T> parent){
				if(node!=null){
					node.setParent(parent);
					if(node==root){
						node.setParent(null);
					}
				}
			}
			
			/**
			 * ����һ����ֵ���Ƴ��ڵ㣻�����ֵ�������У��򷵻ؿ�
			 * @param value ������ֵ
			 * @return the value �Ƴ��ڵ��м�ֵ
			 */
			public  T removeNode  (T value)
			{
				BSTreeNode<T> dataRoot=getRoot();
				BSTreeNode<T> parent=root;
				while(dataRoot!=null)
				{
					int cmp=dataRoot.getValue().compareTo(value);
					if(cmp>0)
					{
						parent=dataRoot;
						dataRoot=dataRoot.getLeft();
					}
					else if(cmp<0)
					{
						parent=dataRoot;
						dataRoot=dataRoot.getRight();
					}
					else // �ҵ�ɾ���ڵ�
					{
						if( dataRoot.getRight()!=null){ //���ӽڵ㲻Ϊ��
							BSTreeNode<T> min=removeMinNode(dataRoot.getRight());
							min.setLeft(dataRoot.getLeft()); 
							setParent(dataRoot.getLeft(),min); 
							if(parent.getLeft()==dataRoot){ 
								parent.setLeft(min); 
							}else{ 
								parent.setRight(min); 
							} 
							setParent(min,parent); 
							if(min!=dataRoot.getRight()){ 
								min.setRight(dataRoot.getRight()); 
								setParent(dataRoot.getRight(),min);
							}
						}else{  // ���ӽڵ�Ϊ�գ�����������ӽڵ�Ϊ�հ�������
							setParent(dataRoot.getLeft(),parent);
							if(parent.getLeft()==dataRoot){
								parent.setLeft(dataRoot.getLeft());
							}else
							{
								parent.setRight(dataRoot.getLeft());
							}
						}
						setParent(dataRoot,null);
						dataRoot.setLeft(null);
						dataRoot.setRight(null);
						if(getRoot()!=null){
							setParent(getRoot(),null);
						}
		                size.decrementAndGet(); 
		                return dataRoot.getValue(); 
					}
				}
				return null;
			}
			
			/**
			 *�ҵ���̽ڵ�
			 * @param node ��ɾ���ڵ�����ӽڵ�
			 * @return
			 */
			private BSTreeNode<T> removeMinNode(BSTreeNode<T>node){
				// �ҵ����ӽڵ�����С�Ľڵ�
				BSTreeNode<T> parent=node;
				while(node!=null && node.getLeft()!=null){
					parent=node;
					node=node.getLeft();
				}
		        //remove min node 
		        if(parent==node){ 
		        	return node;
		        } 
		         parent.setLeft(node.getRight());
		         setParent(node.getRight(),parent);
		        return node; 
			}
			
			/**
			 * debug mode,�÷���������ӡ�����Ľڵ��Լ��������ӽڵ�
			 * ÿһ�������ͬһ��
			 * @param nroot
			 */
			public void printTree(BSTreeNode<T> nroot){
				LinkedList<BSTreeNode<T>> queue=new LinkedList<BSTreeNode<T>>();
				LinkedList<BSTreeNode<T>> queue2=new LinkedList<BSTreeNode<T>>();
				if(nroot==null){
					return ;
				}
				queue.add(nroot);
				boolean firstQueue =true;
				
				while(!queue.isEmpty() || !queue2.isEmpty()){
					LinkedList<BSTreeNode<T>> q=firstQueue?queue:queue2;
					BSTreeNode<T> n=q.poll();
					if(n!=null){
						String pos=n.getParent()==null ? "": (n==n.getParent().getLeft()?"LE":"RI");
						String pstr=n.getParent()==null?"":n.getParent().toString();
						System.out.print(n+"("+pstr+" "+(pos)+")"+"\t");
						if(n.getLeft()!=null){
							(firstQueue?queue2:queue).add(n.getLeft());
						}
						if(n.getRight()!=null){
							(firstQueue?queue2:queue).add(n.getRight());
						}
					}else{
						System.out.println();
						firstQueue=!firstQueue;
					}
				}
				System.out.println("\n");
			}
			public static void main(String[] args){
				BSTree<String> bst=new BSTree<String>();
				bst.addNode("k");
				bst.addNode("e");
				bst.addNode("c");
				bst.addNode("d");
				bst.addNode("b");
				bst.addNode("f");
				bst.printTree(bst.getRoot());
				
				bst.addNode("a");
				bst.addNode("e");
				bst.printTree(bst.getRoot());
				
				bst.addNode("g");
				bst.addNode("h");
				bst.printTree(bst.getRoot());
				
				bst.removeNode("d");
				bst.printTree(bst.getRoot());
			}
}
















