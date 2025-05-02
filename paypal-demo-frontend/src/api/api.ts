import axios from 'axios';
import {Product, Cart} from "@/api/types.ts";

export const fetchProducts = () => axios.get<Product[]>('/api/products');

export const fetchProduct = (id: number) => axios.get<Product>(`/api/products/${id}`);

export const addProduct = (product: Product) =>
    axios.post<number>('/api/products', product); // returns productId (Long)

export const getCart = () => axios.get<Cart>('/api/cart');

export const addToCart = (productId: number, quantity = 1) =>
    axios.post<void>('/api/cart/add', null, {
        params: {productId, quantity},
    });

export const clearCart = () => axios.delete<void>('/api/cart');

export const createPaypalPayment = () =>
    axios.post<string>('/api/cart/paypal/create');

export const getPaypalOrder = (id: string) => axios.get<Cart>(`/api/cart/paypal/order/${id}`);