package com.thhh.easy.dao.imp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.thhh.easy.dao.IActDao;
import com.thhh.easy.entity.Act;
import com.thhh.easy.entity.Users;
import com.thhh.easy.util.Constant;

/**
 * A data access object (DAO) providing persistence and search support for Act
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.thhh.easy.entity.Act
 * @author MyEclipse Persistence Tools
 */
public class ActDAO extends HibernateDaoSupport implements IActDao {
	private static final Logger log = LoggerFactory.getLogger(ActDAO.class);
	// property constants
	public static final String THEME = "theme";
	public static final String ACCOUNT = "account";
	public static final String DAYS = "days";
	public static final String PAY = "pay";
	public static final String STATES = "states";
	public static final String CONTENTS = "contents";

	protected void initDao() {

	}

	/**
	 * 根据id查询活动
	 */
	public Act findActById(int id) {
		log.debug("getting Act instance with id: " + id);
		try {
			Act instance = (Act) getHibernateTemplate().get(
					"com.thhh.easy.entity.Act", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public Users findUserById(int id) {
		log.debug("getting Users instance with id: " + id);
		try {
			Users instance = (Users) getHibernateTemplate().get(
					"com.thhh.easy.entity.Users", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	/**
	 * 查看活动,搜索活动列表，id=user.id
	 */
	public List<Act> findAct(int id, int pageIndex, int rowCount) {

		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		Query query = session
				.createQuery("from Act where users_id not in (?) and states = 1 order by create_date desc");

		query.setInteger(0, id);

		int startRow = (pageIndex - 1) * rowCount;
		query.setFirstResult(startRow);
		query.setMaxResults(rowCount);

		// query.executeUpdate();

		List<Act> actList = query.list();

		// session.close();
		return actList;
	}

	/**
	 * 查看活动详情，id=act.id
	 */
	public Act findActDetail(int id) {

		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();

		Query query = session.createQuery("from Act where id=?");

		query.setInteger(0, id);

		// query.executeUpdate();

		List<Act> actList = query.list();

		if (actList.size() != 0) {
			Act act = (Act) actList.get(0);
			// session.close();
			return act;
		}
		// session.close();

		return null;
	}

	/**
	 * 参加该活动的人数
	 */
	public int countPartici(int actid) {
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();

		Query query = session
				.createQuery("select count(*) from Partici where act_id = ?");

		query.setInteger(0, actid);

		int count = ((Number) query.uniqueResult()).intValue();

		return count;
	}

	/**
	 * 保存活动
	 */
	public void saveAct(Act act) {
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();

		Transaction tx = session.beginTransaction();
		session.save(act);
		tx.commit();

		// session.save(act);
		// session.beginTransaction().commit();

		// log.debug("saving Posts instance");
		// try {
		// getHibernateTemplate().save(transientInstance);
		// log.debug("save successful");
		// } catch (RuntimeException re) {
		// log.error("save failed", re);
		// throw re;
		// }

	}

	/**
	 * 取消活动
	 */
	public void cancelAct(int actid) {
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		// Act act = new Act();

		session.beginTransaction();

		Query query = session
				.createQuery("update Act set states = 4 where id = ?");
		query.setInteger(0, actid);

		query.executeUpdate();
		session.getTransaction().commit();

	}

	/**
	 * 取消活动，判断查询
	 */
	public String checkCancel(int userid, int actid) {
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		Query query = session
				.createQuery("from Act where id =? and users_id =?");
		query.setInteger(0, actid);
		query.setInteger(1, userid);

		List<Act> list = query.list();
		if (list.size() != 0) {

			return Constant.STRING_1;

		} else {

			return Constant.STRING_0;
		}

	}

	public static Logger getLog() {
		return log;
	}

}