package com.thhh.easy.user.service.imp;


import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.thhh.easy.dao.ICollectsDao;
import com.thhh.easy.dao.IImageDao;
import com.thhh.easy.dao.IPostsDao;
import com.thhh.easy.dao.IUsersDao;
import com.thhh.easy.entity.Collects;
import com.thhh.easy.entity.Image;
import com.thhh.easy.entity.Posts;
import com.thhh.easy.entity.Users;
import com.thhh.easy.user.service.IUserService;

public class UserService implements IUserService{

	private IUsersDao usersDao ;
	private IPostsDao postsDao ;
	private ICollectsDao collectsDao ;
	private IImageDao imageDao ;
	
	public Users findUserById(int id) {
		
		return usersDao.findById(id) ;
	}
	//根据用户名查找用户
	public Users findUserByName(String name) {
		
		return usersDao.findByName(name);
	}
	
	//保存用户
	public void save(Users user) {
		usersDao.save(user) ;
	}
	//修改个人信息
	public Users updateUsers(Users u) {
		imageDao.save(u.getImage());
		return usersDao.merge(u);
	}
	//查找用户发帖数
	public int findPostCount(Integer userId) {
		String queryString ="select count(*) from Posts where users_id = "+userId+"" ;
		Session session=postsDao.getHibernateTemplate().getSessionFactory().openSession();
		Query query=session.createQuery(queryString);
		List list =query.list();
		int count =Integer.parseInt(list.get(0).toString());
		return count;
	}
	//用户收藏的帖子数
	public int findCollectCount(Integer userId) {
		String queryString="select count(*) from Collects where users_id = ?";
		Session session = collectsDao.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(queryString);
		List list = query.list();
		int count = Integer.parseInt(list.get(0).toString());
		return count;
	}
	/**
	 * 查看指定用户发的帖
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> findUserPosts(Integer userId) {
		String queryString = "select id from Posts where users_id="+userId+"" ;
		Session session = postsDao.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(queryString);
		List<Posts> list = query.list();
		for (Posts posts : list) {
			list.add(posts);
		}
		return list;
	}
	/**
	 * 查看指定用户所收藏的帖子
	 * @param pageIndex
	 * @param rowCount
	 * @param userId
	 * @return
	 */
	public List<Collects> findUserCollects(Integer userId){
		List<Collects> listCollects = null;
		String queryString = "select count(*) from Collects where users_id="+userId+"";
		Session session = collectsDao.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(queryString);
		listCollects = query.list();
		return listCollects;
		
	}
	
	//查看用户的图片 propertyName users , value Users
	public List<Image> findByProperty(String propertyName, Object value) {
			
		return imageDao.findByProperty(propertyName, value);
			
	}
	//返回某个用户的图片集合
	public List<Image> findUserImage(Integer id){
		String queryString = "select id from Image where users_id="+id+"" ;
		Session session = imageDao.getHibernateTemplate().getSessionFactory().openSession();
		Query query = session.createQuery(queryString);
		List<Image> list = query.list();
		for (Image image : list) {
			list.add(image);
		}
		return list;
		
	}
	
	public IUsersDao getUsersDao() {
		return usersDao;
	}

	public void setUsersDao(IUsersDao usersDao) {
		this.usersDao = usersDao;
	}
	
	public IPostsDao getPostsDao() {
		return postsDao;
	}
	public void setPostsDao(IPostsDao postsDao) {
		this.postsDao = postsDao;
	}
	
	public ICollectsDao getCollectsDao() {
		return collectsDao;
	}
	public void setCollectsDao(ICollectsDao collectsDao) {
		this.collectsDao = collectsDao;
	}
	
	public IImageDao getImageDao() {
		return imageDao;
	}
	public void setImageDao(IImageDao imageDao) {
		this.imageDao = imageDao;
	}
	public Users findAll() {
		
		return usersDao.findAll();
	}

}
