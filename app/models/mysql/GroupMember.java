package models.mysql;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by acmac on 2016/12/08.
 */
@Entity(name = "group_member")
public class GroupMember extends Model {

	@Id
	public String id;

	@Constraints.Required
	public String uid;

	@Constraints.Required
	public int gid;

	public static Model.Find<Long, GroupMember> find = new Model.Find<Long, GroupMember>() {};

	public static Set<Integer> getGidsByUid(String uid) {


		return find.where().eq("uid", uid).findList().stream().map(groupMember -> groupMember.gid).collect(Collectors.toSet());
	}
}