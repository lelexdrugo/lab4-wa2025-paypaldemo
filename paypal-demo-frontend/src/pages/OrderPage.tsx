// src/pages/OrderPage.tsx
import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Box, Heading, Spinner, Text} from "@chakra-ui/react";
import {getPaypalOrder} from "@/api/api.ts";
import {Cart, CartItem} from "@/api/types.ts";


export const OrderPage = () => {
    const {orderId} = useParams<{ orderId: string }>();
    const [cart, setCart] = useState<Cart | undefined>(undefined);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!orderId) return;
        getPaypalOrder(orderId)
            .then((res) => setCart(res.data))
            .catch((err) => setError(err.message))
            .finally(() => setLoading(false));
    }, [orderId]);

    if (loading) return <Spinner/>;
    if (error)
        return (
            <Box p={4}>
                Ordine non trovato
            </Box>

        )

    return (
        <Box p={4}>
            <Heading mb={4}>Order #{cart?.id}</Heading>
            {cart?.status.toString() == "PAID" &&
                <Text>Thank you for your purchase! We have received your payment and your order is now being
                    processed.</Text>
            }
            <Text>Status: {cart?.status}</Text>
            <Text>Total: ${cart?.items?.reduce((sum: number, item: CartItem) => sum + item.product.price * item.quantity, 0).toFixed(2)}</Text>
            <Box mt={4}>
                <Heading size="md" mb={2}>Items:</Heading>
                {cart?.items.map((item: CartItem) => (
                    <Box key={item.id} p={2} borderWidth={1} borderRadius="md" mb={2} display="flex" gap={4} alignItems="flex-start">
                        <img src={item.product.imageUrl} alt={item.product.name} width={"40px"} style={{alignSelf: "center"}} />
                        <Box>
                            <Text><strong>{item.product.name}</strong></Text>
                            <Text>Quantity: {item.quantity}</Text>
                            <Text>Price: ${item.product.price}</Text>
                        </Box>
                    </Box>
                ))}
            </Box>
        </Box>
    );
};
