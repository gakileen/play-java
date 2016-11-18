package ac;


import play.data.validation.Constraints.*;



/**
 * Created by acmac on 2016/05/05.
 */
public class WishesDto {

    @Required
    private String[] keyword;
    @Required
    private String tag;
    @Required
    @Min(0)
    private int bonus;
    @Required
    @Min(0)
    private int anon;


    public String[] getKeyword() {
        return keyword;
    }

    public void setKeyword(String[] keyword) {
        this.keyword = keyword;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getAnon() {
        return anon;
    }

    public void setAnon(int anon) {
        this.anon = anon;
    }
}
