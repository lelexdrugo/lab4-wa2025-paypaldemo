import {useEffect, useState} from "react";
import {clearCart, createPaypalPayment, getCart} from "../api/api";
import {Box, Button, Heading, HStack, Separator, Text, VStack,} from "@chakra-ui/react";
import {CartItem} from "./CartItem";
import {Cart as CartType} from "@/api/types.ts";
import React from "react";

type CartProps = {
    refreshRef?: React.RefObject<() => void>;
};

export const Cart = ({ refreshRef }: CartProps) => {
    const [cart, setCart] = useState<CartType | null>(null);

    const loadCart = async () => {
        const res = await getCart();
        setCart(res.data);
    };

    const handleCheckout = async () => {
        const res = await createPaypalPayment();
         // assumes 2nd link is the PayPal redirect
        window.location.href = res.data;
    };


    // Expose this to parent through ref
    useEffect(() => {
        if (refreshRef) {
            refreshRef.current = loadCart;
        }
    }, [refreshRef]);


    const handleClear = async () => {
        await clearCart();
        await loadCart();
    };



    useEffect(() => {
        loadCart();
    }, []);

    if (!cart) return null;

    return (
        <Box borderWidth="1px" borderRadius="lg" p={4} mt={10}>
            <Heading size="md" mb={4}>
                Your Cart
            </Heading>
            <VStack align="stretch">
                {cart.items.map((item) => (
                    <CartItem key={item.id} {...item} />
                ))}
                <Separator />
                <Text fontWeight="bold">
                    Total: $
                    {cart.items
                        .reduce((sum, item) => sum + item.product.price * item.quantity, 0)
                        .toFixed(2)}
                </Text>
                <HStack>
                    <Button onClick={handleCheckout} colorPalette="teal">
                        Checkout
                    </Button>
                    <Button onClick={handleClear} colorPalette="teal">Clear Cart</Button>
                </HStack>
            </VStack>
        </Box>
    );
};
