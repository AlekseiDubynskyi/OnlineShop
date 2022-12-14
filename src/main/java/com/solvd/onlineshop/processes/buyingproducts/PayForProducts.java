package com.solvd.onlineshop.processes.buyingproducts;

import com.solvd.onlineshop.exceptions.InvalidChoiceException;
import com.solvd.onlineshop.exceptions.InvalidEnteringException;
import com.solvd.onlineshop.mainshop.GiftCode;
import com.solvd.onlineshop.mainshop.Product;
import com.solvd.onlineshop.people.Customer;
import com.solvd.onlineshop.shoppingorders.Payment;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.solvd.onlineshop.processes.signingup.SignUp.GIFTCODES;

public class PayForProducts {
    private static double totalPrice = 0;
    private final static List<String> BASKET = new ArrayList<String>();
    private final static Logger PAYFORPRODUCTS_LOGGER = LogManager.getLogger(PayForProducts.class);
    public final static List<Product> PRODUCTS = new ArrayList<Product>();

    public static void products() {
        PRODUCTS.add(new Product("LP-01", "Precision 5560", "Dell", 1824.34));
        PRODUCTS.add(new Product("LP-02", "MacBook Pro M1", "Apple", 2699));
        PRODUCTS.add(new Product("LP-03", "EliteBook 840 G8", "HP", 1049.95));
        PRODUCTS.add(new Product("ST-01", "iPhone 13 Pro Max", "Apple", 1799));
        PRODUCTS.add(new Product("ST-02", "Pixel 6", "Google", 999.12));
        PRODUCTS.add(new Product("ST-03", "Galaxy S22 Ultra", "Samsung", 1569));
        PRODUCTS.add(new Product("WM-01", "EFLS627", "Electrolux", 1449));
        PRODUCTS.add(new Product("WM-02", "WM3900", "LG", 1200));
        PRODUCTS.add(new Product("WM-03", "WF45R6100A", "Samsung", 1049));
        PRODUCTS.add(new Product("SN-01", "Adizero Adios Pro 2", "Adidas", 220));
        PRODUCTS.add(new Product("SN-02", "Adapt Auto Max", "Nike", 325));
        PRODUCTS.add(new Product("SN-03", "Paradigm 6", "Altra", 170));
        PRODUCTS.add(new Product("GT-01", "Bullet Mustang", "Squier", 351));
        PRODUCTS.add(new Product("GT-02", "Classic Vibe '60s Stratocaster", "Squier", 219));
        PRODUCTS.add(new Product("GT-03", "G5222 6", "Gretsch", 235.51));
    }

    public static void showProducts() {
        PRODUCTS.forEach(product -> PAYFORPRODUCTS_LOGGER.info(product));
    }

    public static void addingProductsToCart() {
        products();
        PAYFORPRODUCTS_LOGGER.info("Enter the ID of wished product:");
        findingByID();
        continueShopping();
    }

    public static void findingByID() {
        Scanner scanner = new Scanner(System.in);
        String wishedProduct = scanner.nextLine();
        boolean notMatchID = true;

        do {
            try {
                for (Product product : PRODUCTS) {
                    if (wishedProduct.equalsIgnoreCase(String.valueOf(product.getProductID()))) {
                        wishedProduct = String.valueOf(product);
                        PAYFORPRODUCTS_LOGGER.info("Your product is added to the shopping cart: " + wishedProduct);
                        BASKET.add(wishedProduct);
                        totalPrice = totalPrice + product.getPrice();
                        PAYFORPRODUCTS_LOGGER.info("Total price for the order is: $" + totalPrice);
                        notMatchID = false;
                    }
                }
                if (notMatchID = true) {
                    continueShopping();
                } else {
                    PAYFORPRODUCTS_LOGGER.info("Invalid ID number. Please try again.");
                    findingByID();
                }
            } catch (InvalidEnteringException e) {
                PAYFORPRODUCTS_LOGGER.error(e);
            }
        } while (!notMatchID);
    }

    public static void continueShopping() {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            PAYFORPRODUCTS_LOGGER.info("Do you want to continue shopping?" + "\n" +
                    "1) Yes. Add some more products to the shopping cart." + "\n" +
                    "2) No. Pay for the order.");
            choice = scanner.nextInt();
            try {
                if (choice == 1) {
                    addingProductsToCart();
                } else if (choice == 2) {
                    PAYFORPRODUCTS_LOGGER.info("Products in your shopping cart: ");
                    BASKET.forEach(product -> PAYFORPRODUCTS_LOGGER.info(product));
                    paymentOrder();
                    break;
                } else {
                    throw new InvalidChoiceException("Invalid entering data. Please enter one of the provided numbers.");
                }
            } catch (InvalidChoiceException e) {
                PAYFORPRODUCTS_LOGGER.error(e);
            }
        } while (choice != 0);
    }

    public static void paymentOrder() {
        Scanner scanner = new Scanner(System.in);
        String cardNumber;
        boolean cardValidation = false;
        boolean confirmPaying = false;
        String customerID;
        String addressCustomer;
        String paymentID = UUID.randomUUID().toString();

        Calendar date = Calendar.getInstance();
        List<String> customerData = new ArrayList<>();

        File customerFile = new File("src/main/resources/payment.txt");
        PAYFORPRODUCTS_LOGGER.info("PAYING FOR THE ORDER.");

        Customer customer = new Customer();

        PAYFORPRODUCTS_LOGGER.info("Enter your ID:");
        customerID = scanner.nextLine();
        customer.setCustomerID(customerID);
        customerData.add("CustomerID: " + customer.getCustomerID());
        PAYFORPRODUCTS_LOGGER.info("Enter your address:");
        addressCustomer = scanner.nextLine();
        customer.setAddress(addressCustomer);
        customerData.add("Customer address: " + customer.getAddress());
        do {
            PAYFORPRODUCTS_LOGGER.info("Enter your card number:");
            cardNumber = scanner.nextLine();
            if (cardNumber.matches("(^4[0-9]{12}(?:[0-9]{3})?$)|(^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$)|(3[47][0-9]{13})|(^3(?:0[0-5]|[68][0-9])[0-9]{11}$)|(^6(?:011|5[0-9]{2})[0-9]{12}$)|(^(?:2131|1800|35\\d{3})\\d{11}$)")) {
                customer.setCardNumber(cardNumber);
                customerData.add("Card numbers: " + customer.getCardNumber());
                cardValidation = true;
            } else {
                PAYFORPRODUCTS_LOGGER.info("The card number is invalid. The card must have 16 numbers.");
            }
        } while (cardValidation != true);

        customerData.add("Date: " + date.getTime());
        customerData.add("Ordered products:" + BASKET);
        discount();
        customerData.add("Total price: $" + totalPrice);

        new Payment(paymentID, customerID, new Date(), true);

        try {
            FileUtils.writeLines(customerFile, customerData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        do {
            PAYFORPRODUCTS_LOGGER.info("Total price to pay: $" + totalPrice + ". Confirm your paying for the order (write 'Confirm')");
            String confirmation = scanner.nextLine();
            if (Objects.equals(confirmation, "Confirm")) {
                confirmPaying = true;
                OrderDetails.orderDetails();
                break;
            } else {
                PAYFORPRODUCTS_LOGGER.info("Please write 'Confirm' to confirm your order.");
            }
        } while (confirmPaying != true);
    }

    public static void discount() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            PAYFORPRODUCTS_LOGGER.info("Total price to pay: $" + totalPrice + ". If you have a gift code, " +
                    "you can enter it for 3% discount. Do you want to use a gift code?" + "\n" +
                    "1) Yes" + "\n" +
                    "2) No");
            choice = scanner.nextInt();

            try {
                if (choice == 1) {
                    matchGiftCode();
                    break;
                } else if (choice == 2) {
                    break;
                } else {
                    PAYFORPRODUCTS_LOGGER.info("Invalid entering data. Please enter one of the provided numbers.");
                    discount();
                }
            } catch (InvalidEnteringException e) {
                PAYFORPRODUCTS_LOGGER.error(e);
            }
        } while (choice != 2);
    }

    public static void matchGiftCode() {
        Scanner scanner = new Scanner(System.in);
        boolean notMatchGiftCode = true;
        int choice;
        PAYFORPRODUCTS_LOGGER.info("Please enter your gift code:");
        String enteredGiftCode = scanner.nextLine();

        do {
            try {
                for (GiftCode giftcode : GIFTCODES) {
                    if (enteredGiftCode.equalsIgnoreCase(giftcode.getGiftCode())) {
                        enteredGiftCode = String.valueOf(giftcode);
                        PAYFORPRODUCTS_LOGGER.info("The gift code is added to your order. You have a 3% discount!");
                        totalPrice = totalPrice - totalPrice * 0.03;
                    }
                } if (true) {
                } else if (notMatchGiftCode = true ) {
                    PAYFORPRODUCTS_LOGGER.info("The gift code does not exist. Choose the next step: " + "\n" +
                            "1) Enter the gift code one more time." + "\n" +
                            "2) Pay for order without gift code.");
                    choice = scanner.nextInt();
                    try {
                        if (choice == 1) {
                            matchGiftCode();
                        } else if (choice == 2) {
                            break;
                        } else {
                            throw new InvalidChoiceException("Invalid entering data. Please enter one of the provided numbers.");
                        }
                    } catch (InvalidChoiceException e) {
                        PAYFORPRODUCTS_LOGGER.error(e);
                    }
                }
            } catch (InvalidEnteringException e) {
                PAYFORPRODUCTS_LOGGER.error(e);
            }
        } while (!notMatchGiftCode);
    }
}

