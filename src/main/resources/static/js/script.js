/* =====================================================
   PRODUCT DATA
===================================================== */
const productData = {

    "realmadrid-black": {
        name: "Short Pattern Oversized Drop Shoulder Real Madrid T-Shirt",
        price: 950,
        color: "Black",
        image: "/images/realmadrid-black.jpeg"
    },

    "realmadrid-white": {
        name: "Short Pattern Oversized Drop Shoulder Real Madrid T-Shirt",
        price: 950,
        color: "White",
        image: "/images/realmadrid-white.jpeg"
    },

    "baggy-black": {
        name: "Korean Baggy",
        price: 1290,
        color: "Black",
        image: "/images/korean-beggy-black.jpeg"
    },

    "baggy-white": {
        name: "Korean Baggy",
        price: 1290,
        color: "White",
        image: "/images/korean-beggy-white.jpeg"
    },

    "kids-frog": {
        name: "Kids Frog",
        price: 490,
        color: "",
        image: "/images/kids-frog.jpeg"
    },

    "women-top": {
        name: "Women's Top",
        price: 650,
        color: "",
        image: "/images/women-top.jpg"
    },

    "women-sharee": {
        name: "Sharee",
        price: 1200,
        color: "",
        image: "/images/women-Sharee.jpg"
    },

    "casual-shoe": {
        name: "Casual Shoe",
        price: 1500,
        color: "",
        image: "/images/casual-shoe.jpg"
    },

    "mens-shoe": {
        name: "Men's Shoe",
        price: 1800,
        color: "",
        image: "/images/shoe.jpg"
    }
};


/* =====================================================
   CURRENT PRODUCT
===================================================== */

let currentProduct = null;

let productQuantity = 1;


/* =====================================================
   LOAD PRODUCT DETAILS
===================================================== */

function loadProductDetails() {

    const productId =
        new URLSearchParams(window.location.search).get("product");

    if (!productId) {
        return;
    }

    const product = productData[productId];

    if (!product) {
        return;
    }

    currentProduct = product;


    const image =
        document.getElementById("productImage");

    const name =
        document.getElementById("productName");

    const price =
        document.getElementById("productPrice");


    if (image) {
        image.src = product.image;
        image.alt = product.name;
    }


    if (name) {
        name.textContent = product.name;
    }


    if (price) {

        if (product.price > 0) {

            price.textContent =
                "৳" + product.price.toLocaleString();

        } else {

            price.textContent =
                "Price available soon";

        }

    }


    /* Hide/show color buttons */

    const blackButton =
        document.getElementById("blackButton");

    const whiteButton =
        document.getElementById("whiteButton");


    if (blackButton && whiteButton) {

        if (productId.includes("black")) {

            blackButton.style.display = "inline-block";
            whiteButton.style.display = "inline-block";

        }

        if (productId.includes("white")) {

            blackButton.style.display = "inline-block";
            whiteButton.style.display = "inline-block";

        }

        if (!productId.includes("black") &&
            !productId.includes("white")) {

            blackButton.style.display = "none";
            whiteButton.style.display = "none";

        }

    }

}


/* =====================================================
   PRODUCT COLOR
===================================================== */

function selectColor(color) {

    if (!currentProduct) {
        return;
    }


    const image =
        document.getElementById("productImage");


    if (currentProduct.name.includes("Real Madrid")) {

        if (color === "Black") {

            currentProduct.color = "Black";

            currentProduct.image =
                "/images/realmadrid-black.jpeg";

            image.src =
                "/images/realmadrid-black.jpeg";

        } else {

            currentProduct.color = "White";

            currentProduct.image =
                "/images/realmadrid-white.jpeg";

            image.src =
                "/images/realmadrid-white.jpeg";
        }

    }


    if (currentProduct.name === "Korean Baggy") {

        if (color === "Black") {

            currentProduct.color = "Black";

            currentProduct.image =
                "/images/korean-beggy-black.jpeg";

            image.src =
                "/images/korean-beggy-black.jpeg";

        } else {

            currentProduct.color = "White";

            currentProduct.image =
                "/images/korean-beggy-white.jpeg";

            image.src =
                "/images/korean-beggy-white.jpeg";
        }

    }

}


/* =====================================================
   PRODUCT QUANTITY
===================================================== */

function increaseProductQuantity() {

    productQuantity++;

    const quantity =
        document.getElementById("productQuantity");

    if (quantity) {
        quantity.textContent = productQuantity;
    }

}


function decreaseProductQuantity() {

    if (productQuantity > 1) {

        productQuantity--;

        const quantity =
            document.getElementById("productQuantity");

        if (quantity) {
            quantity.textContent = productQuantity;
        }

    }

}


/* =====================================================
   ADD TO CART
===================================================== */

function addToCart() {

    if (!currentProduct) {
        return;
    }


    const cartProduct = {

        name: currentProduct.name,

        price: currentProduct.price,

        quantity: productQuantity,

        color: currentProduct.color,

        image: currentProduct.image

    };


    localStorage.setItem(
        "cartProduct",
        JSON.stringify(cartProduct)
    );


    alert("Product added to cart!");


    window.location.href = "/cart";

}


/* =====================================================
   BUY NOW
===================================================== */

function buyNow() {

    if (!currentProduct) {
        return;
    }


    const cartProduct = {

        name: currentProduct.name,

        price: currentProduct.price,

        quantity: productQuantity,

        color: currentProduct.color,

        image: currentProduct.image

    };


    localStorage.setItem(
        "cartProduct",
        JSON.stringify(cartProduct)
    );


    window.location.href = "/checkout";

}


/* =====================================================
   CART
===================================================== */

let cartQuantity = 1;

let cartPrice = 0;

let cartProduct = null;


/* =====================================================
   LOAD CART
===================================================== */

function loadCart() {

    const saved =
        localStorage.getItem("cartProduct");


    if (!saved) {
        return;
    }


    cartProduct =
        JSON.parse(saved);


    cartQuantity =
        cartProduct.quantity;

    cartPrice =
        cartProduct.price;


    const cartName =
        document.getElementById("cartProductName");

    const cartImage =
        document.getElementById("cartProductImage");

    const cartColor =
        document.getElementById("cartProductColor");

    const quantity =
        document.getElementById("quantity");


    if (cartName) {
        cartName.textContent =
            cartProduct.name;
    }


    if (cartImage) {

        cartImage.src =
            cartProduct.image;

        cartImage.alt =
            cartProduct.name;

    }


    if (cartColor) {

        if (cartProduct.color) {

            cartColor.textContent =
                "Color: " + cartProduct.color;

        } else {

            cartColor.textContent = "";

        }

    }


    if (quantity) {
        quantity.textContent =
            cartQuantity;
    }


    updateCart();

}


/* =====================================================
   CART QUANTITY
===================================================== */

function increaseQuantity() {

    cartQuantity++;

    updateCart();

}


function decreaseQuantity() {

    if (cartQuantity > 1) {

        cartQuantity--;

        updateCart();

    }

}


/* =====================================================
   UPDATE CART
===================================================== */

function updateCart() {

    if (!cartProduct) {

        const saved =
            localStorage.getItem("cartProduct");

        if (saved) {

            cartProduct =
                JSON.parse(saved);

            cartPrice =
                cartProduct.price;

        } else {

            cartPrice = 0;

        }

    }


    const subtotal =
        cartPrice * cartQuantity;


    const quantity =
        document.getElementById("quantity");

    const subtotalElement =
        document.getElementById("subtotal");

    const discountElement =
        document.getElementById("discount");

    const totalElement =
        document.getElementById("total");


    if (quantity) {
        quantity.textContent =
            cartQuantity;
    }


    if (subtotalElement) {
        subtotalElement.textContent =
            subtotal.toLocaleString();
    }


    if (discountElement) {
        discountElement.textContent = "0";
    }


    if (totalElement) {
        totalElement.textContent =
            subtotal.toLocaleString();
    }


    if (cartProduct) {

        cartProduct.quantity =
            cartQuantity;

        localStorage.setItem(
            "cartProduct",
            JSON.stringify(cartProduct)
        );

    }

}


/* =====================================================
   REMOVE CART ITEM
===================================================== */

function removeItem() {

    localStorage.removeItem("cartProduct");

    cartProduct = null;

    cartQuantity = 1;

    cartPrice = 0;


    const item =
        document.querySelector(".cart-item");

    if (item) {
        item.remove();
    }


    const subtotal =
        document.getElementById("subtotal");

    const discount =
        document.getElementById("discount");

    const total =
        document.getElementById("total");


    if (subtotal) {
        subtotal.textContent = "0";
    }

    if (discount) {
        discount.textContent = "0";
    }

    if (total) {
        total.textContent = "0";
    }

}


/* =====================================================
   COUPON
===================================================== */

function applyCoupon() {

    if (!cartProduct) {

        const saved =
            localStorage.getItem("cartProduct");

        if (saved) {

            cartProduct =
                JSON.parse(saved);

            cartPrice =
                cartProduct.price;

        }

    }


    const codeInput =
        document.getElementById("couponCode");


    if (!codeInput) {
        return;
    }


    const code =
        codeInput.value.trim().toUpperCase();


    const subtotal =
        cartPrice * cartQuantity;


    let discount = 0;


    if (code === "SAVE10") {

        discount =
            subtotal * 0.10;


        const message =
            document.getElementById("couponMessage");

        if (message) {

            message.textContent =
                "Coupon applied successfully!";

        }

    } else {

        const message =
            document.getElementById("couponMessage");

        if (message) {

            message.textContent =
                "Invalid coupon code.";

        }

    }


    const total =
        subtotal - discount;


    const subtotalElement =
        document.getElementById("subtotal");

    const discountElement =
        document.getElementById("discount");

    const totalElement =
        document.getElementById("total");


    if (subtotalElement) {

        subtotalElement.textContent =
            subtotal.toLocaleString();

    }


    if (discountElement) {

        discountElement.textContent =
            discount.toLocaleString();

    }


    if (totalElement) {

        totalElement.textContent =
            total.toLocaleString();

    }

}


/* =====================================================
   SEARCH
===================================================== */

function searchProduct() {

    const input =
        document.getElementById("searchInput");


    if (!input) {
        return;
    }


    const query =
        input.value.trim();


    if (query === "") {

        alert("Please enter a product name.");

        return;

    }


    window.location.href =
        "/search-results?q=" +
        encodeURIComponent(query);

}


/* =====================================================
   CHECKOUT
===================================================== */

function loadCheckout() {

    const saved =
        localStorage.getItem("cartProduct");


    if (!saved) {
        return;
    }


    const product =
        JSON.parse(saved);


    const summary =
        document.getElementById("checkoutProductName");

    const quantity =
        document.getElementById("checkoutQuantity");

    const total =
        document.getElementById("checkoutTotal");


    if (summary) {

        summary.textContent =
            product.name;

    }


    if (quantity) {

        quantity.textContent =
            product.quantity;

    }


    if (total) {

        total.textContent =
            "৳" +
            (
                product.price *
                product.quantity
            ).toLocaleString();

    }

}


/* =====================================================
   PLACE ORDER
===================================================== */

function placeOrder() {

    const name =
        document.getElementById("name");

    const phone =
        document.getElementById("phone");

    const email =
        document.getElementById("email");

    const address =
        document.getElementById("address");


    if (!name || !phone || !address) {
        return;
    }


    if (
        name.value.trim() === "" ||
        phone.value.trim() === "" ||
        address.value.trim() === ""
    ) {

        alert(
            "Please fill in your name, phone number and delivery address."
        );

        return;

    }


    const saved =
        localStorage.getItem("cartProduct");


    if (!saved) {

        alert("Your cart is empty.");

        return;

    }


    const product =
        JSON.parse(saved);


    const payment =
        document.querySelector(
            'input[name="payment"]:checked'
        );


    const order = {

        orderNumber:
            "ORD" +
            Date.now(),

        date:
            new Date().toLocaleDateString(),

        customerName:
            name.value.trim(),

        phone:
            phone.value.trim(),

        email:
            email
                ? email.value.trim()
                : "",

        address:
            address.value.trim(),

        payment:
            payment
                ? payment.value
                : "cod",

        product:
        product,

        total:
            product.price *
            product.quantity

    };


    localStorage.setItem(
        "lastOrder",
        JSON.stringify(order)
    );


    localStorage.setItem(
        "orderHistory",
        JSON.stringify([order])
    );


    localStorage.removeItem("cartProduct");


    alert("Order placed successfully!");


    window.location.href =
        "/order-history";

}


/* =====================================================
   ORDER HISTORY
===================================================== */

function loadOrderHistory() {

    const saved =
        localStorage.getItem("orderHistory");


    const container =
        document.getElementById("orders");


    if (!container) {
        return;
    }


    if (!saved) {

        container.innerHTML =
            "<p>No orders found.</p>";

        return;

    }


    const orders =
        JSON.parse(saved);


    container.innerHTML = "";


    orders.forEach(function(order) {

        container.innerHTML += `

            <div class="order-card">

                <div class="order-header">

                    <div>

                        <strong>
                            Order #${order.orderNumber}
                        </strong>

                        <p>
                            Placed on: ${order.date}
                        </p>

                    </div>

                    <span class="order-status">
                        Confirmed
                    </span>

                </div>


                <div class="order-product">

                    <div class="order-image">

                        <img
                            src="${order.product.image}"
                            alt="${order.product.name}"
                            style="width:100%;height:100%;object-fit:cover;"
                        >

                    </div>


                    <div class="order-info">

                        <h3>
                            ${order.product.name}
                        </h3>

                        <p>
                            Color:
                            ${order.product.color || "N/A"}
                        </p>

                        <p>
                            Quantity:
                            ${order.product.quantity}
                        </p>

                        <p>
                            Price:
                            ৳${order.product.price.toLocaleString()}
                        </p>

                    </div>


                    <div class="order-total">

                        <strong>
                            ৳${order.total.toLocaleString()}
                        </strong>

                    </div>

                </div>


                <div class="order-footer">

                    <strong>
                        Total:
                        ৳${order.total.toLocaleString()}
                    </strong>

                </div>

            </div>

        `;

    });

}


/* =====================================================
   PAGE INITIALIZATION
===================================================== */

document.addEventListener(
    "DOMContentLoaded",
    function() {

        loadProductDetails();

        loadCart();

        loadCheckout();

        loadOrderHistory();

    }
);