package agency.alterway.edillion.models;

/**
 * Created by marekrigan on 12/06/15.
 */
public class LoginObject
{
    private Customer customer;
    private User user;

    public LoginObject() {}

    public LoginObject(Customer customer, User user)
    {
        this.customer = customer;
        this.user = user;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "LoginObject{" +
                "customer=" + customer.toString() +
                ", user=" + user.toString() +
                '}';
    }
}
